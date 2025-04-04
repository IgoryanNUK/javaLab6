package app.server;

import app.exceptions.CorruptedFile;
import app.exceptions.UnknownException;
import app.product.Product;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.util.*;
import java.util.function.Predicate;

public class CollectionManager {
    private TreeSet<Product> products;
    private final SavingManager savingManager;
    private final Date initDate = new Date();
    private Date lastChangeDate = new Date();

    /**
     * Создаёт виртуальную коллекцию.
     * Если получается, читает объекты коллекции из фала сохранения. Если нет, то создаёт пустую коллекцию и использует другой файл сохранения.
     *
     * @param envVar имя переменной окружения, в которой хранится путь к фалу сохранения
     */
    public CollectionManager(String envVar) {
        savingManager = new SavingManager(envVar);

        try {
            products = savingManager.readSaving();
            if (!validateCollection()) {
                throw new CorruptedFile(System.getenv(envVar));
            }
            setIdsBusy();
        } catch (Exception e) {
            //!!логгирование
            products = new TreeSet<>();
            throw new UnknownException(e);
        }

    }

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

    /**
     * Очищает коллекцию.
     */
    public void clear() {
        products.clear();
        //save();
    }

    /**
     * Удаляет все элементы, удовлетворяющие заданному условию.
     *
     * @param p условие, по которому удаляются объекты
     * @return true, если хотя бы один элемент был удалён из коллекции.
     */
    public boolean removeIf(Predicate<Product> p){
        boolean b = products.removeIf(p);
        //save();
        return b;
    }


    /**
     * Возвращает все элементы, удовлетворяющие заданному условию.
     *
     * @param p условие
     * @return массив продуктов
     */
    public List<Product> getIf(Predicate<Product> p) {
        List<Product> array = new ArrayList<>();
        products.stream().filter(p).forEach(array::add);

        return array.stream().toList();
    }


    public void save() {
        try {
            savingManager.save(products);
        } catch (Exception e) {
            throw new UnknownException(e);
        }
    }

    /**
     * Заполнение коллекции сохранёнными данными.
     *
     * @param filePath абсолютный путь файла, содержащего сохранённые данные коллекции
     */
    public void readCollection(String filePath) throws FileNotFoundException {

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
    private boolean validateCollection() {
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
