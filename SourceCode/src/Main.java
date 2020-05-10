import java.util.Date;
import java.util.Scanner;

class IDgenerator {
    static int lastUsedId = 1001;
    public static int generateId() {
        return lastUsedId++;
    }
}

class Employee {
    private String name;
    private int id;
    private String paymentMode;
    private double dues;
    private boolean isPartofUnion;
    private Date joiningDate;

    // Constructor
    public Employee(String name,String paymentMode) {
        this.name = name;
        this.id = IDgenerator.generateId();
        this.paymentMode = paymentMode;
        this.dues = 0;
        this.isPartofUnion = false;
    }
    public Employee(String name) {
        this(name,"Cash");
    }
    public Employee() { this("","Cash"); }

    public String getName() { return name; }
    public int getId() { return id; }
    public String getPaymentMode() { return paymentMode; }
    public double getDues() { return dues; }
    public boolean hasUnionMemberShip() { return isPartofUnion; }
    public Date getJoiningDate() { return joiningDate; }

    public void updateDue(double newDue) {
        dues = newDue;
    }
    public void updatePaymentMode(String newPaymentMode) {
        paymentMode = newPaymentMode;
    }
    public void getUnionMemberShip() {
        isPartofUnion = true;
    }
    public void dropUnionMembership() {
        isPartofUnion = false;
    }

    public String toString(){
        return name + " " + id;
    }
}

public class Main {
    public static void main(String[] args){
        Scanner scan = new Scanner(System.in);
        

        scan.close();
    }
}
