package ru.zettatech.product;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class Generateproduct {
    public static void main(String[] args) {
        System.out.println(generateInsertInto());
    }

    public static String generateInsertInto() {
        StringBuilder sql = new StringBuilder();

        for (int i = 1; i <= 1000; i++) {
            // id
            sql.append("(").append(i).append(", ");

            // sku
            sql.append(i + 10000).append(", ");

            // quantity
            sql.append(getRandom(10, 1000)).append(", ");

            // image
            sql.append("'image/product/product").append(i).append(".jpg'").append(", ");

            // name
            sql.append("'Product ").append(i).append("', ");

            // description
            sql.append("'The Cinema HD features an active-matrix liquid crystal display that produces flicker-free " +
                    "images that deliver twice the brightness, twice the sharpness and twice the contrast ratio of " +
                    "a typical CRT display. Unlike other flat panels, it is designed with a pure digital interface to " +
                    "deliver distortion-free images that never need adjusting. With over 4 million digital pixels, " +
                    "the display is uniquely suited for scientific and technical applications such as visualizing " +
                    "molecular structures or analyzing geological data.").append("', ");

            // manufacturer_id
            sql.append(getRandom(1, 5)).append(", ");

            // price
            sql.append(getRandom(100, 10000)).append(", ");

            // weight
            sql.append(100 * getRandom(1, 10)).append(", ");

            // status
            sql.append(1).append(", ");

            // viewed
            sql.append(getRandom(100, 10000)).append(", ");

            // date_added
            sql.append("'").append(getRandomDateString()).append("'");

            sql.append("),\n");
        }

        return sql.toString();
    }

    public static int getRandom(int min, int max) {

        int range = (max - min) + 1;
        return (int) ((range * Math.random()) + min);
    }

    public static String getRandomDateString() {
        Random random = new Random();
        Calendar calendar = Calendar.getInstance();

        int year = random.nextInt(4) + 2020;

        // Генерируем случайный месяц (0-11)
        int month = random.nextInt(12);

        // Устанавливаем год и месяц, чтобы получить максимальное количество дней в месяце
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        // Генерируем случайный день (1 до maxDay)
        int day = random.nextInt(maxDay) + 1;

        // Генерируем случайные час, минуту и секунду
        int hour = random.nextInt(24);
        int minute = random.nextInt(60);
        int second = random.nextInt(60);

        // Устанавливаем значения в календарь
        calendar.set(year, month, day, hour, minute, second);

        // Получаем дату и форматируем
        Date date = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        return sdf.format(date);
    }
}
