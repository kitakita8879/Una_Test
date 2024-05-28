package com.example.una_test;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class AbstractFactoryPatternActivity extends AppCompatActivity {
    private static final String PREF_NAME_ORDER = "order";
    private TextView txtShow;
    private AbstractDessertFactory mFactory;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abstract_factory_pattern);

        SharedPreferences order = getSharedPreferences(PREF_NAME_ORDER, MODE_PRIVATE);
        SharedPreferences.Editor editor = order.edit();

        TextView txtOrder = findViewById(R.id.txt_order);
        txtShow = findViewById(R.id.txt_show);
        findViewById(R.id.btn_1).setOnClickListener(v -> {
            bakery(Taste.CHOCOLATE);
            AbstractDrink drink = mFactory.createDrink(true);
            txtShow.setText(String.format("%s price %s size %s", drink.getDrinkName(),
                    drink.getDrinkPrice(), drink.getDrinkSize()));
            editor.putString("Drink1", String.valueOf(txtShow.getText()));
            editor.apply();
            txtOrder.setText(String.valueOf(order.getAll()));
        });
        findViewById(R.id.btn_2).setOnClickListener(v -> {
            bakery(Taste.CHOCOLATE);
            AbstractDessert dessert = mFactory.createDessert();
            txtShow.setText(String.format("%s price %s", dessert.getDessertName(),
                    dessert.getDessertPrice()));
            editor.putString("Dessert1", String.valueOf(txtShow.getText()));
            editor.apply();
            txtOrder.setText(String.valueOf(order.getAll()));
        });
        findViewById(R.id.btn_3).setOnClickListener(v -> {
            bakery(Taste.BLUEBERRY);
            AbstractDrink drink = mFactory.createDrink(false);
            txtShow.setText(String.format("%s price %s size %s", drink.getDrinkName(),
                    drink.getDrinkPrice(), drink.getDrinkSize()));
            editor.putString("Drink2", String.valueOf(txtShow.getText()));
            editor.apply();
            txtOrder.setText(String.valueOf(order.getAll()));
        });
        findViewById(R.id.btn_4).setOnClickListener(v -> {
            bakery(Taste.BLUEBERRY);
            AbstractDessert dessert = mFactory.createDessert();
            txtShow.setText(String.format("%s price %s", dessert.getDessertName(),
                    dessert.getDessertPrice()));
            editor.putString("Dessert2", String.valueOf(txtShow.getText()));
            editor.apply();
            txtOrder.setText(String.valueOf(order.getAll()));
        });
        findViewById(R.id.btn_clear).setOnClickListener(v -> {
            editor.clear();
            editor.apply();
            txtOrder.setText(String.valueOf(order.getAll()));
        });
    }

    private void bakery(Taste taste) {
        if (Objects.requireNonNull(taste) == Taste.CHOCOLATE) {
            mFactory = new ChocolateFactory(taste);
        } else {
            mFactory = new BlueBerryFactory(taste);
        }
    }

    private abstract static class AbstractDessert {
        abstract String getDessertName();

        abstract int getDessertPrice();
    }

    private static class Pudding extends AbstractDessert {

        private final String mTaste;
        private final int mTastePrice;

        @Override
        String getDessertName() {
            return mTaste + "Pudding";
        }

        @Override
        int getDessertPrice() {
            return mTastePrice * 50;
        }

        Pudding(Taste taste) {
            this.mTaste = taste.toString();
            this.mTastePrice = taste.equals(Taste.CHOCOLATE) ? 2 : 1;
        }
    }

    private abstract static class AbstractDrink {
        abstract String getDrinkName();

        abstract int getDrinkPrice();

        abstract String getDrinkSize();
    }

    private static class Milk extends AbstractDrink {

        private final String mTaste, mSize;
        private final int mPrice;

        @Override
        String getDrinkName() {
            return mTaste + "milk";
        }

        @Override
        int getDrinkPrice() {
            return mPrice;
        }

        @Override
        String getDrinkSize() {
            return mSize;
        }

        Milk(Taste taste, boolean isLarge) {
            this.mTaste = taste.toString();
            this.mPrice = isLarge ? 60 : 30;
            this.mSize = isLarge ? "Large" : "Small";
        }
    }

    private abstract static class AbstractDessertFactory {

        abstract AbstractDessert createDessert();

        abstract AbstractDrink createDrink(boolean isLarge);
    }

    private static class ChocolateFactory extends AbstractDessertFactory {

        private final Taste mTaste;

        @Override
        AbstractDessert createDessert() {
            return new Pudding(mTaste);
        }

        @Override
        AbstractDrink createDrink(boolean isLarge) {
            return new Milk(mTaste, isLarge);
        }

        ChocolateFactory(Taste taste) {
            this.mTaste = taste;
        }
    }

    private static class BlueBerryFactory extends AbstractDessertFactory {

        private final Taste mTaste;

        @Override
        AbstractDessert createDessert() {
            return new Pudding(mTaste);
        }

        @Override
        AbstractDrink createDrink(boolean isLarge) {
            return new Milk(mTaste, isLarge);
        }

        BlueBerryFactory(Taste taste) {
            this.mTaste = taste;
        }
    }

    private enum Taste {
        CHOCOLATE, BLUEBERRY;

        @NonNull
        @Override
        public String toString() {
            switch (this) {
                case CHOCOLATE:
                    return "Chocolate";
                case BLUEBERRY:
                    return "Blueberry";
                default:
                    return "";
            }
        }
    }
}
