import java.time.DayOfWeek;
import java.time.format.TextStyle;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Employee implements Observer{
    private String name;
    private String surname;
    private int id;
    private Manager manager;
    private Warehouse warehouse;
    private WorkingWeek weekSchedule;

    public Employee(String name,String surname,int id,Manager manager,Warehouse warehouse){
        this.name = name;
        this.surname = surname;
        this.id = id;
        this.manager = manager; //è necessario che ogni istanza di Employee sia associata a un Manager specifico, allora ha senso includere il Manager come parte del costruttore.Questo potrebbe essere utile se ogni dipendente ha un manager diretto a cui riferire, e questa associazione è fissa.
        this.warehouse = warehouse;
        this.weekSchedule = new WorkingWeek();
    }

    public String getName(){
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname(){
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Dipendete [nome= "+name+" ,surname= "+surname+" ,id= "+id+" ]";
    }

    //riguarda metodo
    @Override
    public void update(String message){ //riguarda
        System.out.println("Dipendente: " + name +" id: "+ id + " ha ricevuto l'aggiornamento dal manager: " + message);
    }

    public  void addProduct(Product product){//riguarda
        warehouse.addProduct(product);
    }

    public void removeProduct(Product product){//riguarda
        warehouse.removeProducts(product);
    }

    public List<Product> getListProducts(){
        return warehouse.getProducts();
    }

    public void modifyPrice(Product product, double newPrice){ //riguarda
        product.setPrice(newPrice);

        String message = "Il Dipendente "+ name +" ha modificato il prezzo del prodotto: "+ product.getName() + " a "+newPrice;
        manager.notify(this,message);
    }

    public void modifyQuantity(Product product,int newQuantity){ //riguarda
        product.setQuantity(newQuantity);

        String message = "Il Dipendente "+ name +" ha modificato la quantità del prodotto: "+ product.getName() + " a "+newQuantity;
        manager.notify(this,message);
    }

    public void viewAllProducts(){//riguarda
        warehouse.viewProducts();
    }

    public void viewProductsByType(String type){//riguarda
        warehouse.viewProductsByType(type);
    }

    public void notifyProductOutOfStock(){//riguarda perchè a volte non notifica
        List<Product> productList = warehouse.getProducts();
        for (Product product:productList) {
            if (product.getQuantity() == 0) {
                String message = "Il dipendente " + name + " ha notificato che il prodotto " + product.getName() + " è esaurito";
                manager.notify(this,message);
            }
        }
    }

    public void notifyExpiredProduct(String type){ //riguarda
        Date today = new Date(System.currentTimeMillis());
        long millisOneDay = 24 * 60 * 60 * 1000; //millis in un giorno

        for (Product product: warehouse.getProductsByType(type)){
            Date expiredDate = product.getExpiryDate();
            if(expiredDate != null){
                long difference = expiredDate.getTime() - today.getTime();
                if(difference <= millisOneDay){
                    String message = "Il dipendente "+name+" dice che il prodotto "+product.getName()+" scade domani, rimuovere dal magazzino!";
                    manager.notify(this,message);
                }
            }
        }
    }

    public WorkingWeek getWeekSchedule(){ //riguarda
        return weekSchedule;
    }

    public String viewWeekSchedule(){ //riguarda
        WorkingWeek weekSchedule = getWeekSchedule();
        StringBuilder scheduleString = new StringBuilder();

        for (DayOfWeek day: DayOfWeek.values()){
            DailySchedule dailySchedule = weekSchedule.getDailySchedule(day);
            if(dailySchedule != null){
                scheduleString.append(day.getDisplayName(TextStyle.FULL, Locale.getDefault())).append(": ").append(dailySchedule.getStartTime()).append(" - ").append(dailySchedule.getEndTime()).append("\n");
            }
        }
        return scheduleString.toString();
    }
}
