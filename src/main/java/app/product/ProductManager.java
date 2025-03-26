package app.product;

import app.exceptions.UnknownException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.util.*;
import java.util.function.Predicate;

/** Класс управления коллекцией продуктов. */
public class ProductManager {
    private TreeSet<Product> products = new TreeSet<>();
    private final Date initDate = new Date();
    private Date lastChangeDate = new Date();

    /** Добавляет продукт в коллекцию. */
    public void addProduct(Product p) {
        products.add(p);
        lastChangeDate = new Date();
    }       

    /** Возвращает тип коллекции. */
    public String getCollectionName() {
        return products.getClass().getName();
    }

    public String getInitDate() {return initDate.toString();}

    public String getLastChangeDate() {return lastChangeDate.toString();}

    public int getSize() {return products.size();}

    public Product getProductById(int id) {
        List<Product> list =  products.stream().filter(e -> e.getId() == id).toList();
        if (list.isEmpty()) return null;
        else return list.getFirst();
    }

    /** Выводит все элементы коллекции. */
    public void println() {
        for (Product p : products) {
            System.out.println(p.print());
        }
    }

    /**
     * Очищает коллекцию.
     */
    public void clear() {
        products.clear();
    }

    /**
     * Удаляет все элементы, удовлетворяющие заданному условию.
     *
     * @param p условие, по которому удаляются объекты
     */
    public boolean removeIf(Predicate<Product> p){
        return products.removeIf(p);
    }


    /**
     * Выводит все элементы, удовлетворяющие заданному условию.
     *
     * @param p условие, по которому выводятся объекты
     */
    public void printIf(Predicate<Product> p) {
        for (Product product : products) {
            if (p.test(product)) {
                System.out.println(product.print());
            }
        }
    }

    /**
     * Сохраняет коллекцию в файл. Данные сохраняются в формате json.
     *
     * @param filePath абсолютный путь файла, в который сохраняется коллекция
     */
    public void saveCollection(String filePath) throws IOException {
        OutputStreamWriter fileWriter = new OutputStreamWriter(new FileOutputStream(filePath));
        ObjectMapper objectMapper = new ObjectMapper();

        List<Product> productList = products.stream().toList();

        String jsonArray = objectMapper.writeValueAsString(productList);

        fileWriter.write(jsonArray);

        fileWriter.close();

    }

    /**
     * Заполнение коллекции сохранёнными данными.
     *
     * @param filePath абсолютный путь файла, содержащего сохранённые данные коллекции
     */
    public void readCollection(String filePath) throws FileNotFoundException{
        try (InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(filePath))) {
            ObjectMapper objectMapper = new ObjectMapper();
            List<Product> productList = objectMapper.readValue(inputStreamReader, new TypeReference<List<Product>>() {
            });
            products = new TreeSet<>(productList);
            if (!productsValidation()) {
                System.out.println("!!! Не удалось прочитать некоторые из сохранённых данных, так как они были повреждены !!!");
            }
            setIdsBusy();
            System.out.println("***Сохранённые данные успешно прочитаны***");
        } catch (FileNotFoundException f) {
            throw f;
        } catch (Exception e) {
            throw new UnknownException(e.toString());
        }
    }

    /**
     * Сообщает классу Product, какие id уже заняты.
     */
    private void setIdsBusy() {
        products.forEach(e -> Product.setIdBusy(e.getId()));
    }

    /**
     * Проверяет валидность объектов коллекции.
     * Если какой-то объект не обладает нужным полем, то он удаляется из коллекции.
     *
     * @return true, если все объекты валидны, иначе false
     */
    private boolean productsValidation() {
        Iterator<Product> it = products.iterator();
        boolean ifCorrect = true;

        while (it.hasNext()) {
            Product p = it.next();
            if (!(p.getName() != null && p.getCoordinates() != null && p.getCreationDate() != null &&
                    p.getPrice() != null && p.getUnitOfMeasure() != null && p.getOwner() != null)) {
                it.remove();
                ifCorrect = false;
            }
        }

        return ifCorrect;
    }
}
