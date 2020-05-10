import java.util.Date;
import java.util.Scanner;

class IDgenerator {
    static int lastUsedId = 1001;
    public static int generateId() {
        return lastUsedId++;
    }
}

class EmployeeUnion {
    public static double dueRate;  //   per week
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
        this.joiningDate = new Date();
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

    public double calculateDues(){
        double d = getDues();
        if(hasUnionMemberShip()){
            d = d + EmployeeUnion.dueRate;
        }
        return d;
    }

    public String toString(){
        return name + " " + id;
    }
}

class HourlyPaidEmployee extends Employee {
    private double hourRate;
    private int[] hoursWorked;            // from Mon to Fri
    public static int hourLimit = 8;
    public static double overtimeRate = 1.5;
    public static double defaultHourRate = 500;

    public final static String[] DAYS = {"MON","TUE","WED","THU","FRI"};
    enum WEEKDAYS {MON,TUE,WED,THU,FRI};

    // Constructor
    public HourlyPaidEmployee(String name,String paymentMode,double hourRate){
        super(name,paymentMode);
        this.hourRate = hourRate;
        hoursWorked = new int[5];
        for(int i=0;i<5;i++) hoursWorked[i] = 0;
    }
    public HourlyPaidEmployee(String name,double hourRate){
        this(name,"Cash",hourRate);
    }
    public HourlyPaidEmployee(String name) { this(name,"Cash",defaultHourRate); }
    public HourlyPaidEmployee() { this("","Cash",0.0); }

    public int getHoursWorked(int d) { return hoursWorked[d]; }
    public double getHourRate() { return hourRate; }
    public void updateWorkingHour(String dayofWork,int workDone) {
        int d = WEEKDAYS.valueOf(dayofWork).ordinal();
        hoursWorked[d] = workDone;
    }

    public double calculatePayment() {
        double sm = 0;
        for(int i=0;i<5;i++){
            sm = sm + Math.min(hourLimit, hoursWorked[i])*hourRate + Math.max(0.0, hoursWorked[i]-hourLimit)*(overtimeRate)*hourRate;
        }
        double d = calculateDues();
        if(sm >= d){ sm -= d; d=0; }
        else { d -= sm; sm = 0; }
        updateDue(d);
        return sm;
    }

    public void getPaySlip(){
        System.out.println("Name : " + getName());
        System.out.println("EmpID : " + getId());

        for(int i=0;i<5;i++){
            System.out.println("Hours Worked on " + DAYS[i] + " " + getHoursWorked(i));
        }
        System.out.println("Total Dues : " + calculateDues());
        System.out.println("Total Payable Amount : " + calculatePayment());
    }
}

public class Main {
    public static void main(String[] args){
        Scanner scan = new Scanner(System.in);

//        System.out.println("ok");
//        System.out.println("Enter the details : ");
//        System.out.println("Enter the Name : ");
//        String name = scan.next();
//        HourlyPaidEmployee emp = new HourlyPaidEmployee(name);
//
//        double p = emp.calculatePayment();
//        System.out.println(p);
//        emp.getPaySlip();
//
//        emp.updateWorkingHour("MON",10);
//        emp.getPaySlip();

        

        scan.close();
    }
}
