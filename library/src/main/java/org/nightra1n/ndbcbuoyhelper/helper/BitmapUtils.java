package org.nightra1n.ndbcbuoyhelper.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;

public class BitmapUtils {

	public static Bitmap RotateBitmap(Bitmap bitmap, float angle) {
		Matrix matrix = new Matrix();
		matrix.preRotate(angle);
		return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(),
				matrix, true);
	}

	// Convert a view to bitmap
	public static Bitmap createDrawableFromView(Context context, View view) {
		DisplayMetrics displayMetrics = new DisplayMetrics();
		((AppCompatActivity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
		view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
		view.buildDrawingCache();
		Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

		Canvas canvas = new Canvas(bitmap);
		view.draw(canvas);

		return bitmap;
	}

    public static Bitmap textToBitmap(Context context, String text, float textSize, int textColor) {
//        Paint paint = new Paint();
//        paint.setTextSize(convertToPixels(context, (int) textSize));
//        paint.setColor(textColor);
//        paint.setTextAlign(Paint.Align.CENTER);
//        int width = (int) (paint.measureText(text) + 0.5f); // round
//        float baseline = (int) (-paint.ascent() + 0.5f); // ascent() is negative
//        int height = (int) (baseline + paint.descent() + 0.5f);
//
//        Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(image);
//        canvas.drawText(text, 0, baseline, paint);


        TextPaint textPaint = new TextPaint();
        textPaint.setTextSize(convertToPixels(context, (int) textSize));
        textPaint.setColor(textColor);
        textPaint.setTextAlign(Paint.Align.CENTER);

        // http://daniel-codes.blogspot.com/2013/10/centering-single-line-text-in-canvas.html
        int textPaintWidth = (int) (textPaint.measureText(text) + 0.5f); // round
        float textPaintBaseline = (int) (-textPaint.ascent() + 0.5f); // ascent() is negative
        int textPaintHeight = (int) (textPaintBaseline + textPaint.descent() + 0.5f);

        Bitmap image = Bitmap.createBitmap(textPaintWidth, textPaintHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(image);

        StaticLayout staticLayout = new StaticLayout(text, textPaint, canvas.getWidth(),
                Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
        canvas.save();
        // calculate the x and y position where your text will be placed
        float textX = 0f;
        float textY = textPaintBaseline;

//        int xPos = (canvas.getWidth() / 2);
        float xPos = ((canvas.getWidth() - textPaint.getTextSize() * Math.abs(text.length() / 2f)) / 2);
        float yPos = ((canvas.getHeight() / 2f) - ((textPaint.descent() + textPaint.ascent()) / 2f)) ;
        // ((textPaint.descent() + textPaint.ascent()) / 2) is the distance from the baseline to the center.

//        canvas.translate(textX, textY);
        canvas.translate(xPos, yPos);
        staticLayout.draw(canvas);
        canvas.restore();

        return image;
    }

    public static Bitmap writeTextOnDrawable(Context context, int drawableId, String text) {
        Drawable drawable = context.getResources().getDrawable(drawableId);
        return writeTextOnDrawable(context, drawable, text);
    }

    public static Bitmap writeTextOnDrawable(Context context, Drawable drawable, String text) {
        Bitmap bm = drawableToBitmap(drawable);

        Typeface tf = Typeface.create("Helvetica", Typeface.BOLD);

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        paint.setTypeface(tf);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(convertToPixels(context, 11));

        Rect textRect = new Rect();
        paint.getTextBounds(text, 0, text.length(), textRect);

        Canvas canvas = new Canvas(bm);

        // TODO: enlarge canvas for text

        //If the text is bigger than the canvas , reduce the font size
        if(textRect.width() >= (canvas.getWidth() - 4))     //the padding on either sides is considered as 4, so as to appropriately fit in the text
            paint.setTextSize(convertToPixels(context, 7));        //Scaling needs to be used for different dpi's


        //Calculate the positions
        int xPos = (canvas.getWidth() / 2) - 2;     //-2 is for regulating the x position offset

        //"- ((paint.descent() + paint.ascent()) / 2)" is the distance from the baseline to the center.
        int yPos = (int) ((canvas.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2) + 6) ;

        canvas.drawText(text, xPos, yPos, paint);

        return  bm;
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable)drawable).getBitmap();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    public static Drawable bitmapToDrawable(Context context, Bitmap bitmap) {
        return new BitmapDrawable(context.getResources(), bitmap);
    }

	public static Bitmap writeTextOnTopOfDrawable(Context context, int drawableId, String text) {

		Bitmap bm = BitmapFactory.decodeResource(context.getResources(), drawableId)
				.copy(Bitmap.Config.ARGB_8888, true);

		Drawable iconDrawable = context.getResources().getDrawable(drawableId);
		final int iconWidth = iconDrawable.getIntrinsicWidth();
		final int iconHeight = iconDrawable.getIntrinsicHeight();

		Typeface tf = Typeface.create("Helvetica", Typeface.BOLD);

		Paint paint = new Paint();
		paint.setStyle(Paint.Style.FILL);
		paint.setColor(Color.WHITE);
		paint.setTypeface(tf);
		paint.setTextAlign(Paint.Align.CENTER);
		paint.setTextSize(convertToPixels(context, 11));

		Rect textRect = new Rect();
		paint.getTextBounds(text, 0, text.length(), textRect);

		int combinedWidth = (textRect.width() > bm.getWidth()) ? textRect.width() : bm.getWidth();
		int combinedHeight = textRect.height() + bm.getHeight();

		Bitmap bmOverlay = Bitmap.createBitmap(combinedWidth, combinedHeight, Bitmap.Config.ARGB_8888);

//		Canvas canvas = new Canvas(bm);
		Canvas canvas = new Canvas(bmOverlay);

		iconDrawable.draw(canvas);

		// TODO: enlarge canvas for text

		//If the text is bigger than the canvas , reduce the font size
		if(textRect.width() >= (canvas.getWidth() - 4))     //the padding on either sides is considered as 4, so as to appropriately fit in the text
			paint.setTextSize(convertToPixels(context, 7));        //Scaling needs to be used for different dpi's


		//Calculate the positions
		int xPos = (canvas.getWidth() / 2) - 2;     //-2 is for regulating the x position offset

		//"- ((paint.descent() + paint.ascent()) / 2)" is the distance from the baseline to the center.
		int yPos = (int) ((canvas.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2) + 6) ;

//		canvas.drawText(text, xPos, yPos, paint);
		canvas.drawText(text, combinedWidth / 2, combinedHeight / 4 * 3, paint);

		canvas.drawBitmap(bm, combinedWidth / 2 - 2, combinedHeight / 2, null);

		return  bm;
	}

	public static int convertToPixels(Context context, int nDP) {
		final float conversionScale = context.getResources().getDisplayMetrics().density;
		return (int) ((nDP * conversionScale) + 0.5f) ;
	}
}
