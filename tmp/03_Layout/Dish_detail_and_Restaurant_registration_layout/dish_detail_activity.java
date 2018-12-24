
// dùng để test activity_dish_detail.xml

package com.example.test;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class dish_detail_activity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dish_detail);

        ImageView dish_avatar = (ImageView) findViewById(R.id.img_dish_avatar);
        dish_avatar.setImageDrawable(getDrawable(R.drawable.bo_kho));

        LinearLayout mGallery = (LinearLayout) findViewById(R.id.scrll_dish_image_list);

        for (int i = 0; i < 10; i++)
        {
            View item = getLayoutInflater().inflate(
                    R.layout.dish_image_list_item, null);
            item.setId(i);
            ImageView image = (ImageView) item.findViewById(R.id.img_dish_image_list_item);
            image.setImageResource(R.drawable.bo_kho);

            mGallery.addView(item);
        }

        LinearLayout side_dishes = (LinearLayout) findViewById(R.id.scrll_side_dishes);
        View item = getLayoutInflater().inflate(
                R.layout.side_dish_list_item, null);
        item.setId(0);

        ImageView image = (ImageView) item.findViewById(R.id.img_dish_avatar);
        image.setImageResource(R.drawable.banh_mi);

        TextView dish_name = (TextView) item.findViewById(R.id.txt_dish_name);
        dish_name.setText("Bánh mì");

        TextView dish_price = (TextView) item.findViewById(R.id.txt_dish_price);
        dish_price.setText("3000VNĐ");

        side_dishes.addView(item);
    }
}
