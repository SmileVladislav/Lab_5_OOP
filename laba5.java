import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

class SweetException extends Exception {
    public SweetException(String message) {
        super(message);
    }
}

abstract class Sweet {
    private String name;
    private double weight;
    private double chocolateContent;
    private double price;

    public Sweet(String name, double weight, double chocolateContent, double price) throws SweetException {
        if (weight <= 0) {
            throw new SweetException("Вага цукерки '" + name + "' повинна бути більшою за нуль.");
        }
        if (chocolateContent < 0 || chocolateContent > 100) {
            throw new SweetException("Вміст шоколаду в '" + name + "' повинен бути від 0 до 100%.");
        }
        if (price <= 0) {
            throw new SweetException("Ціна цукерки '" + name + "' повинна бути більшою за нуль.");
        }
        this.name = name;
        this.weight = weight;
        this.chocolateContent = chocolateContent;
        this.price = price;
    }

    public String getName() { return name; }
    public double getWeight() { return weight; }
    public double getChocolateContent() { return chocolateContent; }
    public double getPrice() { return price; }

    @Override
    public String toString() {
        return String.format("%s: Вага=%.1fг, Шоколад=%.1f%%, Ціна=%.2f₴",
                name, weight, chocolateContent, price);
    }
}

class ChocolateCandy extends Sweet {
    private String cocoaType;

    public ChocolateCandy(String name, double weight, double chocolateContent, double price, String cocoaType) throws SweetException {
        super(name, weight, chocolateContent, price);
        this.cocoaType = cocoaType;
    }

    @Override
    public String toString() {
        return super.toString() + String.format(" [Тип шоколаду: %s]", cocoaType);
    }
}

class Caramel extends Sweet {
    private boolean hasFilling;

    public Caramel(String name, double weight, double chocolateContent, double price, boolean hasFilling) throws SweetException {
        super(name, weight, chocolateContent, price);
        this.hasFilling = hasFilling;
    }

    @Override
    public String toString() {
        return super.toString() + (hasFilling ? " [З начинкою]" : " [Без начинки]");
    }
}

class Jelly extends Sweet {
    private String flavor;

    public Jelly(String name, double weight, double chocolateContent, double price, String flavor) throws SweetException {
        super(name, weight, chocolateContent, price);
        this.flavor = flavor;
    }

    @Override
    public String toString() {
        return super.toString() + String.format(" [Смак: %s]", flavor);
    }
}

class Gift {
    private List<Sweet> sweets;

    public Gift() {
        sweets = new ArrayList<>();
    }

    public void addSweet(Sweet sweet) {
        if (sweet == null) {
            throw new IllegalArgumentException("Неможливо додати порожню цукерку (null).");
        }
        sweets.add(sweet);
    }

    public double getTotalWeight() {
        double total = 0;
        for (Sweet sweet : sweets) {
            total += sweet.getWeight();
        }
        return total;
    }

    public void sortByWeight() {
        sweets.sort(Comparator.comparingDouble(Sweet::getWeight));
    }

    public List<Sweet> findSweetsByChocolate(double minChocolate, double maxChocolate) {
        if (minChocolate < 0 || maxChocolate > 100 || minChocolate > maxChocolate) {
            throw new IllegalArgumentException("Некоректно заданий діапазон вмісту шоколаду.");
        }

        List<Sweet> foundSweets = new ArrayList<>();
        for (Sweet sweet : sweets) {
            if (sweet.getChocolateContent() >= minChocolate && sweet.getChocolateContent() <= maxChocolate) {
                foundSweets.add(sweet);
            }
        }
        return foundSweets;
    }

    public void printGift() {
        System.out.println("Вміст подарунка ");
        for (Sweet sweet : sweets) {
            System.out.println(sweet.toString());
        }
        System.out.println("=====");
    }
}

public class laba5 {
    public static void main(String[] args) {
        Gift myGift = new Gift();

        try {
            System.out.println("- Формування подарунка -");
            myGift.addSweet(new ChocolateCandy("Корона", 100, 75.0, 55.0, "Чорний"));
            myGift.addSweet(new ChocolateCandy("Milka", 90, 45.0, 48.5, "Молочний"));
            myGift.addSweet(new Caramel("Ромашка", 15, 20.0, 5.0, true));
            myGift.addSweet(new Caramel("Барбарис", 12, 0.0, 4.5, false));
            myGift.addSweet(new Jelly("Шалена Бджілка", 20, 0.0, 10.0, "Вишня"));

            myGift.printGift();
            System.out.printf("Загальна вага подарунка: %.1fг\n\n", myGift.getTotalWeight());

            System.out.println("-Після сортування за вагою -");
            myGift.sortByWeight();
            myGift.printGift();

            double minChoco = 15.0;
            double maxChoco = 50.0;
            System.out.printf("\n-Пошук цукерок з вмістом шоколаду від %.1f%% до %.1f%% -\n", minChoco, maxChoco);
            List<Sweet> found = myGift.findSweetsByChocolate(minChoco, maxChoco);

            if (found.isEmpty()) {
                System.out.println("Цукерок у такому діапазоні не знайдено.");
            } else {
                for (Sweet s : found) {
                    System.out.println(s.toString());
                }
            }

        } catch (SweetException | IllegalArgumentException e) {
            System.err.println("Помилка під час роботи з подарунком: " + e.getMessage());
        }
    }
}