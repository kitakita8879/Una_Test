package com.example.una_test;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class AbstractFactoryPatternActivity extends AppCompatActivity {
    private TextView txtShow;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abstract_factory_pattern);

        txtShow = findViewById(R.id.txt_show);
        findViewById(R.id.btn_1).setOnClickListener(v -> bakery(Taste.CHOCOLATE, DessertType.CAKE));
        findViewById(R.id.btn_2).setOnClickListener(v -> bakery(Taste.CHOCOLATE, DessertType.PUDDING));
        findViewById(R.id.btn_3).setOnClickListener(v -> bakery(Taste.BLUEBERRY, DessertType.CAKE));
        findViewById(R.id.btn_4).setOnClickListener(v -> bakery(Taste.BLUEBERRY, DessertType.PUDDING));
    }

    private void bakery(Taste taste, DessertType DessertType) {
        AbstractDessertFactory factory;
        AbstractDessert dessert;
        if (Objects.requireNonNull(DessertType) == AbstractFactoryPatternActivity.DessertType.CAKE) {
            factory = new CakeFactory();
        } else {
            factory = new PuddingFactory();
        }
        dessert = factory.createDessert(taste);
        txtShow.setText(String.format("%s price %s ",
                dessert.getDessertName(), dessert.getDessertPrice()));
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

    private static class Cake extends AbstractDessert {
        private final String mTaste;
        private final int mTastePrice;

        @Override
        String getDessertName() {
            return mTaste + "Cake";
        }

        @Override
        int getDessertPrice() {
            return mTastePrice * 100;
        }

        Cake(Taste taste) {
            this.mTaste = taste.toString();
            this.mTastePrice = taste.equals(Taste.CHOCOLATE) ? 2 : 1;
        }
    }

    private abstract static class AbstractDessertFactory {
        abstract AbstractDessert createDessert(Taste taste);
    }

    private static class PuddingFactory extends AbstractDessertFactory {

        @Override
        AbstractDessert createDessert(Taste taste) {
            return new Pudding(taste);
        }
    }

    private static class CakeFactory extends AbstractDessertFactory {

        @Override
        AbstractDessert createDessert(Taste taste) {
            return new Cake(taste);
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

    private enum DessertType {
        PUDDING, CAKE;

        @NonNull
        @Override
        public String toString() {
            switch (this) {
                case PUDDING:
                    return "pudding";
                case CAKE:
                    return "cake";
                default:
                    return "";
            }
        }
    }
}
