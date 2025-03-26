package app.product;

import app.exceptions.UnknownException;
import app.product.builders.CoordinatesBuilder;
import app.product.builders.PersonBuilder;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * Класс, реализующий ввод значений полей объектов.
 */
public class EnterManager {
    /**
     * Реализует ввод имён объектов.
     * При вводе пустой строки выводит сообщение о некорректности значения имени и повторяет ввод.
     *
     * @param enter входной поток, в который вводится значение
     * @param fieldName название вводимого значения
     * @return имя объекта
     * @throws IOException
     */
    public static String nameEnter(BufferedReader enter, String fieldName, boolean isScript) throws IOException {
        String name;
        do {
            System.out.printf("Введите " + fieldName + ": ");
            name = enter.readLine();
            if (name != null && !name.equals("")) {
                if (isScript) System.out.println(name);
                return name;
            }
            else
                System.out.println("(⊙ _ ⊙) " + fieldName + " не может являться пустой строкой. Пожалуйста, повторите ввод");
        } while ((!isScript || enter.ready()) && name.equals(""));
        return null;
    }

    /**
     * Реализет ввод координат.
     * Требует, чтобы координаты являлись вещественным числом.
     *
     * @param enter входной поток, в который вводится значение
     * @return объект кординат
     */
    public static Coordinates coordinatesEnter(BufferedReader enter, boolean isScript) throws IOException{
        String wem = "(⊙ _ ⊙) Координата может принимать только вещественное число (большее 0). Пожалуйста, повторите попытку.";
        CoordinatesBuilder cb = new CoordinatesBuilder();
        boolean isEntered = false;
        do {
            System.out.printf("Введите координату x: ");
            try {
                Double x = Double.valueOf(enter.readLine());
                cb.setX(x);
                if (isScript) System.out.println(x);
                isEntered = true;
            } catch (NumberFormatException e) {
                System.out.println(wem);
            }
        } while (!isEntered && (!isScript || enter.ready()));


        isEntered = false;
        do {
            System.out.printf("Введите координату y: ");
            try {
                Double y = Double.valueOf(enter.readLine());
                cb.setY(y);
                if (isScript) System.out.println(y);
                isEntered = true;
            } catch (NumberFormatException e) {
                System.out.println(wem);
            }
        } while (!isEntered && (!isScript || enter.ready()));

        return cb.createCoordinates();
    }

    /**
     * Реализует ввод вещественного значения.
     * Требует, чтобы значение являлось вещественным числом, большим 0.
     *
     * @param enter входной поток, в который вводится значение
     * @param fieldName назвние параметра, для которого вводится значение
     * @return значение
     */
    public static float floatEnter(BufferedReader enter, String fieldName, boolean isScript) throws IOException{
        String wem = "(⊙ _ ⊙) " + fieldName + " может принимать только вещественное число, большее 0. Пожалуйста, повторите попытку.";
        float fl = 0;
        while (fl == 0 && (!isScript || enter.ready())) {
            System.out.printf("Введите " + fieldName + ": ");
            try {
                fl = Float.valueOf(enter.readLine());
                fl = fl > 0 ? fl : 0;
            } catch (NumberFormatException e) {}
            if (fl == 0) System.out.println(wem);
            else if (isScript) System.out.println(fl);
        }

        return fl;
    }

    /**
     * Реализует ввод партийного номера продукта.
     * Требует, чтобы номер являлся строкой длины от 23 до 51, при вводе пустой строки возвращает null.
     *
     * @param enter входной поток, в который вводится значение
     * @return партийный номер продукта
     */
    public static String partNumberEnter(BufferedReader enter, boolean isScript) {
        try {
            String pN = null;
            boolean isEntered = false;
            String wem = "(⊙ _ ⊙) Партийный номер должен быть уникальным, а также не должен бать длиннее 51 символа и короче 23 символов. Пожалуйста, повторите попытку.";

            while (!isEntered && (!isScript || enter.ready())) {
                System.out.printf("Введите ПАРТИЙНЫЙ номер ([ENTER] пропустить поле): ");
                pN = enter.readLine();
                if (pN.equals("")) {
                    pN = null;
                    isEntered = true;
                } else if (!Product.isPartNumberBusy(pN) && pN.length() >= 23 && pN.length() <= 51) {
                    isEntered = true;
                } else {
                    System.out.println(wem);
                }
                if (isEntered && isScript) System.out.println(pN);
            }

            return pN;
        } catch (Exception e) {
            throw new UnknownException(e.toString());
        }
    }

    /**
     * Реализует ввод стоимости производства продукта.
     * Повторяет ввод, пока значение не будет удовлетворять ограничениям
     *
     * @param enter
     * @return
     * @throws IOException
     */
    public static Double manufactureCostEnter(BufferedReader enter, boolean isScript) throws IOException {
        Double mC = null;

        String s = null;
        while (s == null && (!isScript || enter.ready())) {
            System.out.printf("Введите стоимость производства продукта ([ENTER] пропустить поле): ");
            s = enter.readLine();
            if (!s.equals("")) {
                try {
                    mC = Double.valueOf(s);
                } catch (NumberFormatException e) {
                    System.out.println("(⊙ _ ⊙) Неверный формат. Пожалуйста, введите вещественное число, или пропустите это поле.");
                    s = null;
                }
            }
            if (s != null && isScript) System.out.println(mC);
        }

        return mC;
    }

    /**
     * Реализует ввод пользователя перечисляемого типа.
     * Если не запущен скрипт, то ввод будет запрашиваться до тех пор, пока пользователь не введёт валидное значение.
     *
     * @param enter поток ввода
     * @param values значения, которые может принимать перечисляемый тип
     * @param fieldName название поля, для которого вводится значение
     * @param isNullExceptable флаг, отвечающий за то, приемлемо ли значение null для вводимого поля
     * @param isScript обозначает, читается ли сейчас скрипт
     * @return значение перечисляемого типа, введённое пользователеем, или null (если приемлем)
     * @param <E> перечисляемый тип
     * @throws IOException
     */
    public static <E extends Enum> E enumEnter(BufferedReader enter, E[] values, String fieldName, boolean isNullExceptable, boolean isScript) throws IOException{
        while (!isScript || enter.ready()) {
            System.out.printf("Введите " + fieldName + " (");
            int i = 0;
            for (E u : values) {
                System.out.printf(u.toString());
                i++;
                if (values.length != i) {
                    System.out.printf(" / ");
                }
            }
            System.out.printf("): ");

            String s = enter.readLine().trim().toUpperCase();
            if (isNullExceptable && s.equals("")) return null;
            for (E u : values) {
                if (s.equals(u.toString())) {
                    if (isScript) System.out.println(u);
                    return u;
                }
            }
            System.out.println("(⊙ _ ⊙) Неверный формат. Пожалуйста, введите один из предложенных значений.");
        }
        return null;
    }

    /**
     * Реализует ввод поля, являющимся классом человека.
     * Поочередно зпрашивает ввод имени, роста, цвета глаз, национальности (необязательное поле).
     *
     * @param enter входной поток
     * @param isScript обозначет то, читается ли сейчас скрипт
     * @return объект человека, введённого пользователем
     * @throws IOException
     */
    public static Person personEnter(BufferedReader enter, boolean isScript) throws IOException {
        PersonBuilder pb = new PersonBuilder();

        pb.setName(nameEnter(enter, "имя владельца", isScript));
        pb.setHeight(floatEnter(enter, "рост владельца", isScript));
        pb.setEyeColor(enumEnter(enter, Color.values(), "цвет глаз владельца", false, isScript));
        pb.setNationality(enumEnter(enter, Country.values(), "национальность владельца", true, isScript));

        return pb.createPerson();
    }

}
