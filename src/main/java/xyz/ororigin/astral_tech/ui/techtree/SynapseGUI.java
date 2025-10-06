package xyz.ororigin.astral_tech.ui.techtree;

import icyllis.modernui.animation.Animator;
import icyllis.modernui.animation.AnimatorListener;
import icyllis.modernui.animation.ValueAnimator;
import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.annotation.Nullable;
import icyllis.modernui.core.Context;
import icyllis.modernui.fragment.Fragment;
import icyllis.modernui.graphics.Canvas;
import icyllis.modernui.graphics.Image;
import icyllis.modernui.graphics.Paint;
import icyllis.modernui.graphics.Rect;
import icyllis.modernui.graphics.drawable.ColorDrawable;
import icyllis.modernui.graphics.drawable.Drawable;
import icyllis.modernui.util.DataSet;
import icyllis.modernui.view.*;
import icyllis.modernui.widget.FrameLayout;
import icyllis.modernui.widget.ImageView;

import static icyllis.modernui.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static icyllis.modernui.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class SynapseGUI extends Fragment {

    private static boolean sShouldPlayAnimation = true;
    private FrameAnimDrawable mFrameAnimDrawable;
    private ViewGroup mMainContent;
    private ImageView mAnimationView;
    private AspectRatioFrameLayout mRootLayout;

    /**
     * 设置下次是否播放进入动画
     *
     * @param playAnimation true表示播放动画，false表示跳过动画直接显示主界面
     */
    public static void setShouldPlayAnimation(boolean playAnimation) {
        sShouldPlayAnimation = playAnimation;
        // 这里可以添加保存到配置文件的逻辑（将来完善）
        // saveToConfig(playAnimation);
    }

    /**
     * 获取当前是否播放动画的设置
     *
     * @return true表示会播放动画，false表示跳过动画
     */
    public static boolean shouldPlayAnimation() {
        return sShouldPlayAnimation;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable DataSet savedInstanceState) {
        var context = requireContext();
        mRootLayout = new AspectRatioFrameLayout(context, 16f / 9f);

        // 1. 先创建动画视图（在16:9区域内显示动图）
        mAnimationView = new ImageView(context);
        mFrameAnimDrawable = new FrameAnimDrawable();
        mAnimationView.setImageDrawable(mFrameAnimDrawable);

        // 设置动画视图的布局参数，居中显示
        var animParams = new FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT);
        animParams.gravity = Gravity.CENTER;
        mAnimationView.setLayoutParams(animParams);

        // 2. 创建主内容视图（初始时隐藏）
        mMainContent = createMainContent(context);
        mMainContent.setVisibility(View.GONE); // 先隐藏

        // 添加到根布局
        mRootLayout.addView(mAnimationView);
        mRootLayout.addView(mMainContent);

        return mRootLayout;
    }


    @Override
    public void onStart() {
        super.onStart();
        if (sShouldPlayAnimation) {
            startEntryAnimation();
        } else {
            // 如果不播放动画，直接显示主内容
            showMainContent();
        }
    }

    private void startEntryAnimation() {
        // 开始帧动画
        mFrameAnimDrawable.start();

        // 设置动画结束监听
        mFrameAnimDrawable.setAnimationListener(new FrameAnimDrawable.AnimationListener() {
            @Override
            public void onAnimationEnd() {
                // 动画结束后直接显示主内容，不移除动画视图（避免黑色闪烁）
                showMainContent();
            }
        });
    }


    private void showMainContent() {
        mMainContent.setVisibility(View.VISIBLE);
    }

    private ViewGroup createMainContent(Context context) {
        var content = new FrameLayout(context);
        var params = new FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT);
        params.gravity = Gravity.CENTER;
        content.setLayoutParams(params);
        setupDemoContent(content, context);
        return content;
    }

    private void setupDemoContent(FrameLayout content, Context context) {
        var demoText = new icyllis.modernui.widget.TextView(context);
        demoText.setText("Welcome to ModernUI!");
        demoText.setTextSize(24f);
        demoText.setTextColor(0xFFFFFFFF);
        demoText.setGravity(Gravity.CENTER);
        var textParams = new FrameLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        textParams.gravity = Gravity.CENTER;
        content.addView(demoText, textParams);
        content.setBackground(new ColorDrawable(0xFF2D2D2D));
    }


    public static class AspectRatioFrameLayout extends FrameLayout {
        private final float mAspectRatio;

        public AspectRatioFrameLayout(Context context, float aspectRatio) {
            super(context);
            this.mAspectRatio = aspectRatio;
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            int width = MeasureSpec.getSize(widthMeasureSpec);
            int height = MeasureSpec.getSize(heightMeasureSpec);

            // 计算16:9的尺寸 - 始终居中
            int layoutWidth, layoutHeight;

            // 计算最佳尺寸，确保16:9比例且不超过屏幕
            if (width / (float) height > mAspectRatio) {
                // 屏幕比16:9更宽，根据高度计算宽度
                layoutHeight = height;
                layoutWidth = (int) (height * mAspectRatio);
            } else {
                // 屏幕比16:9更高，根据宽度计算高度
                layoutWidth = width;
                layoutHeight = (int) (width / mAspectRatio);
            }

            // 创建新的测量规格
            int newWidthMeasureSpec = MeasureSpec.makeMeasureSpec(layoutWidth, MeasureSpec.EXACTLY);
            int newHeightMeasureSpec = MeasureSpec.makeMeasureSpec(layoutHeight, MeasureSpec.EXACTLY);

            // 测量所有子视图
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                child.measure(newWidthMeasureSpec, newHeightMeasureSpec);
            }

            // 设置自己的尺寸
            setMeasuredDimension(layoutWidth, layoutHeight);
        }

        @Override
        protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
            int width = getMeasuredWidth();
            int height = getMeasuredHeight();
            int parentWidth = ((View) getParent()).getWidth();
            int parentHeight = ((View) getParent()).getHeight();

            // 计算在父容器中的居中位置
            int layoutLeft = (parentWidth - width) / 2;
            int layoutTop = (parentHeight - height) / 2;

            // 布局所有子视图，填满整个16:9区域
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                child.layout(0, 0, width, height);
            }

            // 设置自己的位置
            setLeft(layoutLeft);
            setTop(layoutTop);
            setRight(layoutLeft + width);
            setBottom(layoutTop + height);
        }
    }

    /**
     * 自定义帧动画Drawable
     */
    public static class FrameAnimDrawable extends Drawable {

        private Image[] mFrames;
        private int mCurrentFrame = 0;
        private ValueAnimator mAnimator;
        private AnimationListener mListener;
        private boolean mIsRunning = false;
        public FrameAnimDrawable() {
            loadFrames();
            setupAnimator();
        }

        private void loadFrames() {
            mFrames = new Image[37];
            for (int i = 0; i < mFrames.length; i++) {
                mFrames[i] = Image.create("astral_tech", "gui/syos/open_animation/" + i + ".jpg");
                // 如果图片不存在，使用占位色块
                if (mFrames[i] == null) {
                    mFrames[i] = createPlaceholderImage(i);
                }
            }
        }

        private Image createPlaceholderImage(int frameIndex) {
            // 创建占位图片用于测试
            // 实际使用时请确保你的图片资源存在
            return null; // 返回null会跳过绘制
        }

        private void setupAnimator() {
            mAnimator = ValueAnimator.ofInt(0, mFrames.length - 1);
            mAnimator.setDuration(2500); // 2秒完成所有帧
            mAnimator.setRepeatCount(0); // 只播放一次

            mAnimator.addUpdateListener(anim -> {
                mCurrentFrame = (int) anim.getAnimatedValue();
                invalidateSelf(); // 触发重绘
            });

            mAnimator.addListener(new AnimatorListener() {
                @Override
                public void onAnimationEnd(@NonNull Animator animation) {
                    mIsRunning = false;
                    if (mListener != null) {
                        mListener.onAnimationEnd();
                    }
                }
            });
        }

        public void start() {
            if (!mIsRunning && mFrames != null && mFrames.length > 0) {
                mIsRunning = true;
                mCurrentFrame = 0;
                mAnimator.start();
            }
        }

        public void setAnimationListener(AnimationListener listener) {
            mListener = listener;
        }

        @Override
        public void draw(@NonNull Canvas canvas) {
            if (mFrames == null || mCurrentFrame >= mFrames.length || mFrames[mCurrentFrame] == null) {
                // 绘制占位背景
                drawPlaceholder(canvas);
                return;
            }

            Rect bounds = getBounds();
            Paint paint = Paint.obtain();

            // 绘制当前帧，居中显示并保持16:9
            Image frame = mFrames[mCurrentFrame];

            // 计算在16:9区域内的最佳显示尺寸
            float frameAspect = (float) frame.getWidth() / frame.getHeight();
            float containerAspect = (float) bounds.width() / bounds.height();

            float scale;
            float drawWidth, drawHeight;
            float drawLeft, drawTop;

            if (frameAspect > containerAspect) {
                // 图片比容器宽，根据宽度缩放
                scale = (float) bounds.width() / frame.getWidth();
                drawWidth = bounds.width();
                drawHeight = frame.getHeight() * scale;
                drawLeft = 0;
                drawTop = (bounds.height() - drawHeight) / 2; // 垂直居中
            } else {
                // 图片比容器高，根据高度缩放
                scale = (float) bounds.height() / frame.getHeight();
                drawWidth = frame.getWidth() * scale;
                drawHeight = bounds.height();
                drawLeft = (bounds.width() - drawWidth) / 2; // 水平居中
                drawTop = 0;
            }

            // 正确的drawImage调用 - 9个参数
            canvas.drawImage(frame,
                    0, 0, frame.getWidth(), frame.getHeight(),  // 源矩形：整个图像
                    drawLeft, drawTop, drawLeft + drawWidth, drawTop + drawHeight,  // 目标矩形
                    paint);
            paint.recycle();
        }

        private void drawPlaceholder(@NonNull Canvas canvas) {
            Rect bounds = getBounds();
            Paint paint = Paint.obtain();

            // 绘制背景
            paint.setColor(0xFF1E1E1E);
            canvas.drawRect(bounds, paint);

            // 绘制边框
            paint.setColor(0xFF444444);
            paint.setStrokeWidth(2f);
            paint.setStyle(Paint.STROKE);
            canvas.drawRect(bounds, paint);

            paint.recycle();
        }

        @Override
        public int getIntrinsicWidth() {
            return 1600; // 16:9 的宽度基准
        }

        @Override
        public int getIntrinsicHeight() {
            return 900; // 16:9 的高度基准
        }

        @Override
        public void setAlpha(int alpha) {
            // 实现透明度支持
        }

        @Override
        public void setColorFilter(@Nullable icyllis.modernui.graphics.ColorFilter colorFilter) {
            // 实现颜色过滤
        }

        public interface AnimationListener {
            void onAnimationEnd();
        }

    }
}