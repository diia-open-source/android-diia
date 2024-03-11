package ua.gov.diia.ui_base.views.pager;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import ua.gov.diia.ui_base.R;
import ua.gov.diia.ui_base.views.pager.attachers.PagerAttacher;
import ua.gov.diia.ui_base.views.pager.attachers.ViewPager2Attacher;

@SuppressWarnings("unused")
public class ScrollingPagerIndicator extends View {

    @IntDef({RecyclerView.HORIZONTAL})
    public @interface Orientation {
    }

    private final float dotMinimumSize;
    private final int dotNormalSize;
    private final float dotSelectedSize;
    private final int spaceBetweenDotCenters;
    private int visibleDotCount;
    private int visibleDotThreshold;
    private int orientation;

    private float visibleFramePosition;
    private float visibleFrameWidth;

    private float firstDotOffset;
    private SparseArray<Float> dotScale;

    private int itemCount;

    @ColorInt
    private int dotColor;

    @ColorInt
    private int selectedDotColor;

    private Runnable attachRunnable;
    private PagerAttacher<?> currentAttacher;

    private boolean dotCountInitialized;

    private int currentPage = 0;

    private Paint paint;

    private static final int MINIMIZED_POINTS_THRESHOLD = 3;

    public ScrollingPagerIndicator(Context context) {
        this(context, null);
    }

    public ScrollingPagerIndicator(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, R.attr.scrollingPagerIndicatorStyle);
    }

    public ScrollingPagerIndicator(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray attributes = context.obtainStyledAttributes(
                attrs, R.styleable.ScrollingPagerIndicator, defStyleAttr, R.style.ScrollingPagerIndicator);
        int nonSelectedId = attributes.getResourceId(R.styleable.ScrollingPagerIndicator_spi_dotColor, R.color.black_alpha_30);
        int selectedId = attributes.getResourceId(R.styleable.ScrollingPagerIndicator_spi_dotSelectedColor, R.color.black);
        dotColor = ContextCompat.getColor(context, nonSelectedId);
        selectedDotColor = ContextCompat.getColor(context, selectedId);
        dotNormalSize = attributes.getDimensionPixelSize(R.styleable.ScrollingPagerIndicator_spi_dotSize, 0);
        dotSelectedSize = dotNormalSize * 1.16f;
        dotMinimumSize = dotNormalSize * 0.83f;

        spaceBetweenDotCenters = attributes.getDimensionPixelSize(R.styleable.ScrollingPagerIndicator_spi_dotSpacing, 0) + dotNormalSize;
        int visibleDotCount = attributes.getInt(R.styleable.ScrollingPagerIndicator_spi_visibleDotCount, 0);
        setVisibleDotCount(visibleDotCount);
        visibleDotThreshold = attributes.getInt(R.styleable.ScrollingPagerIndicator_spi_visibleDotThreshold, 2);
        orientation = attributes.getInt(R.styleable.ScrollingPagerIndicator_spi_orientation, RecyclerView.HORIZONTAL);
        attributes.recycle();

        if (isInEditMode()) {
            setDotCount(visibleDotCount);
            onPageScrolled(visibleDotCount / 2, 0);
        }

        paint = new Paint();
        paint.setAntiAlias(true);

    }

    /**
     * Maximum number of dots which will be visible at the same time.
     * If pager has more pages than visible_dot_count, indicator will scroll to show extra dots.
     * Must be odd number.
     *
     * @return visible dot count
     */
    public int getVisibleDotCount() {
        return visibleDotCount;
    }

    /**
     * Sets visible dot count. Maximum number of dots which will be visible at the same time.
     * If pager has more pages than visible_dot_count, indicator will scroll to show extra dots.
     * Must be odd number.
     *
     * @param visibleDotCount visible dot count
     * @throws IllegalStateException when pager is already attached
     */
    public void setVisibleDotCount(int visibleDotCount) {
        this.visibleDotCount = visibleDotCount;

        if (attachRunnable != null) {
            reattach();
        } else {
            requestLayout();
        }
    }

    /**
     * The minimum number of dots which should be visible.
     * If pager has less pages than visibleDotThreshold, no dots will be shown.
     *
     * @return visible dot threshold.
     */
    public int getVisibleDotThreshold() {
        return visibleDotThreshold;
    }

    /**
     * Sets the minimum number of dots which should be visible.
     * If pager has less pages than visibleDotThreshold, no dots will be shown.
     *
     * @param visibleDotThreshold visible dot threshold.
     */
    public void setVisibleDotThreshold(int visibleDotThreshold) {
        this.visibleDotThreshold = visibleDotThreshold;
        if (attachRunnable != null) {
            reattach();
        } else {
            requestLayout();
        }
    }

    /**
     * The visible orientation of the dots
     *
     * @return dot orientation (RecyclerView.HORIZONTAL, RecyclerView.VERTICAL)
     */
    @Orientation
    public int getOrientation() {
        return orientation;
    }

    /**
     * Set the dot orientation
     *
     * @param orientation dot orientation (RecyclerView.HORIZONTAL, RecyclerView.VERTICAL)
     */
    public void setOrientation(@Orientation int orientation) {
        this.orientation = orientation;
        if (attachRunnable != null) {
            reattach();
        } else {
            requestLayout();
        }
    }

    /**
     * Attaches indicator to ViewPager2
     *
     * @param pager pager to attach
     */
    public void attachToPager(@NonNull ViewPager2 pager) {
        attachToPager(pager, new ViewPager2Attacher());
    }

    /**
     * Attaches to any custom pager
     *
     * @param pager    pager to attach
     * @param attacher helper which should setup this indicator to work with custom pager
     */
    public <T> void attachToPager(@NonNull final T pager, @NonNull final PagerAttacher<T> attacher) {
        detachFromPager();
        attacher.attachToPager(this, pager);
        currentAttacher = attacher;

        attachRunnable = () -> {
            itemCount = -1;
            attachToPager(pager, attacher);
        };
    }

    /**
     * Detaches indicator from pager.
     */
    public void detachFromPager() {
        if (currentAttacher != null) {
            currentAttacher.detachFromPager();
            currentAttacher = null;
            attachRunnable = null;
        }
        dotCountInitialized = false;
    }

    /**
     * Detaches indicator from pager and attaches it again.
     * It may be useful for refreshing after adapter count change.
     */
    public void reattach() {
        if (attachRunnable != null) {
            attachRunnable.run();
            invalidate();
        }
    }

    /**
     * This method must be called from ViewPager.OnPageChangeListener.onPageScrolled or from some
     * similar callback if you use custom PagerAttacher.
     *
     * @param page   index of the first page currently being displayed
     *               Page position+1 will be visible if offset is nonzero
     * @param offset Value from [0, 1) indicating the offset from the page at position
     */

    public void onPageScrolled(int page, float offset) {
        if (offset < 0 || offset > 1) {
            //throw new IllegalArgumentException("Offset must be [0, 1]");
        } else if (page < 0 || page != 0 && page >= itemCount) {
            //throw new IndexOutOfBoundsException("page must be [0, adapter.getItemCount())");
        }

        dotScale.clear();

        scaleDotByOffset(page, offset);

        if (page < itemCount - 1) {
            scaleDotByOffset(page + 1, 1 - offset);
        } else if (itemCount > 1) {
            scaleDotByOffset(0, 1 - offset);
        }
        invalidate();

        adjustFramePosition(offset, page);
        invalidate();
    }

    /**
     * Sets dot count
     *
     * @param count new dot count
     */
    public void setDotCount(int count) {
        initDots(count);
    }

    /**
     * Sets currently selected position (according to your pager's adapter)
     *
     * @param position new current position
     */
    public void setCurrentPosition(int position) {
        currentPage = position;
        if (position != 0 && (position < 0 || position >= itemCount)) {
            //throw new IndexOutOfBoundsException("Position must be [0, adapter.getItemCount()]");
        }
        if (itemCount == 0) {
            return;
        }
        adjustFramePosition(0, position);
        updateScaleInIdleState(position);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // Width
        float measuredWidth;
        // Height
        float measuredHeight;

        // We ignore widthMeasureSpec because width is based on visibleDotCount
        if (isInEditMode()) {
            // Maximum width with all dots visible
            measuredWidth = (visibleDotCount - 1) * spaceBetweenDotCenters + dotSelectedSize;
        } else {
            measuredWidth = itemCount >= visibleDotCount
                    ? (int) visibleFrameWidth
                    : (itemCount - 1) * spaceBetweenDotCenters + dotSelectedSize;
        }
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        // Height
        float desiredHeight = dotSelectedSize;

        switch (heightMode) {
            case MeasureSpec.EXACTLY:
                measuredHeight = heightSize;
                break;
            case MeasureSpec.AT_MOST:
                measuredHeight = Math.min(desiredHeight, heightSize);
                break;
            case MeasureSpec.UNSPECIFIED:
            default:
                measuredHeight = desiredHeight;
        }

        setMeasuredDimension(Math.round(measuredWidth), Math.round(measuredHeight));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int dotCount = itemCount;
        if (dotCount < visibleDotThreshold) {
            return;
        }

        int firstVisibleDotPos = Math.round((visibleFramePosition - firstDotOffset) / spaceBetweenDotCenters);
        int lastVisibleDotPos = firstVisibleDotPos
                + (int) (visibleFramePosition + visibleFrameWidth - getDotOffsetAt(firstVisibleDotPos))
                / spaceBetweenDotCenters;

        // If real dots count is less than we can draw inside visible frame, we move lastVisibleDotPos
        // to the last item
        if (firstVisibleDotPos == 0 && lastVisibleDotPos + 1 > dotCount) {
            lastVisibleDotPos = dotCount - 1;
        }
        int size = getWidth();
        for (int i = firstVisibleDotPos; i <= lastVisibleDotPos; i++) {
            float dot = getDotOffsetAt(i);
            if (dot >= visibleFramePosition && dot < visibleFramePosition + visibleFrameWidth) {
                float diameter;
                float scale;

                // Calculate scale according to current page position
                scale = getDotScaleAt(i);
                diameter = dotNormalSize + (dotSelectedSize - dotNormalSize) * scale;

                boolean isFarFromSelected = Math.abs(currentPage - i) > 1;

                if (itemCount > MINIMIZED_POINTS_THRESHOLD) {
                    if ((i == firstVisibleDotPos || i == lastVisibleDotPos) && isFarFromSelected) {
                        diameter = dotMinimumSize;
                    }
                }

                final int currentColor;
                if (i == currentPage) {
                    currentColor = selectedDotColor;
                } else {
                    currentColor = dotColor;
                }
                paint.setColor(currentColor);

                canvas.drawCircle(dot - visibleFramePosition,
                        getMeasuredHeight() / 2.0f,
                        diameter / 2,
                        paint);

            }
        }
    }

    private void updateScaleInIdleState(int currentPos) {
        dotScale.clear();
        dotScale.put(currentPos, 1f);
        invalidate();

    }

    private void initDots(int itemCount) {
        if (this.itemCount == itemCount && dotCountInitialized) {
            return;
        }
        this.itemCount = itemCount;
        dotCountInitialized = true;
        dotScale = new SparseArray<>();

        if (itemCount < visibleDotThreshold) {
            requestLayoutWithGuarantee();
            return;
        }

        firstDotOffset = dotSelectedSize / 2.0f;
        visibleFrameWidth = (visibleDotCount - 1) * spaceBetweenDotCenters + dotSelectedSize;

        requestLayoutWithGuarantee();
    }

    private void requestLayoutWithGuarantee() {
        if (isInLayout()) {
            post(this::requestLayout);
        } else {
            requestLayout();
        }
    }

    private void adjustFramePosition(float offset, int pos) {
        if (itemCount <= visibleDotCount) {
            // Without scroll
            visibleFramePosition = 0;
        } else {
            // Not looped with scroll
            float center = getDotOffsetAt(pos) + spaceBetweenDotCenters * offset;
            visibleFramePosition = center - visibleFrameWidth / 2;

            // Block frame offset near start and end
            int firstCenteredDotIndex = visibleDotCount / 2;
            float lastCenteredDot = getDotOffsetAt(itemCount - 1 - firstCenteredDotIndex);
            if (visibleFramePosition + visibleFrameWidth / 2 < getDotOffsetAt(firstCenteredDotIndex)) {
                visibleFramePosition = getDotOffsetAt(firstCenteredDotIndex) - visibleFrameWidth / 2;
            } else if (visibleFramePosition + visibleFrameWidth / 2 > lastCenteredDot) {
                visibleFramePosition = lastCenteredDot - visibleFrameWidth / 2;
            }
        }
    }

    private void scaleDotByOffset(int position, float offset) {
        if (dotScale == null || itemCount == 0) {
            return;
        }
        setDotScaleAt(position, 1 - Math.abs(offset));
    }

    private float getDotOffsetAt(int index) {
        return firstDotOffset + index * spaceBetweenDotCenters;
    }

    private float getDotScaleAt(int index) {
        Float scale = dotScale.get(index);
        if (scale != null) {
            return scale;
        }
        return 0;
    }

    private void setDotScaleAt(int index, float scale) {
        if (scale == 0) {
            dotScale.remove(index);
        } else {
            dotScale.put(index, scale);
        }
    }
}