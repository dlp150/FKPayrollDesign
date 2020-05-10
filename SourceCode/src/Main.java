import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Scanner;
import java.util.Collections;

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
    public Employee(String name,int id,String paymentMode,Date joiningDate) {
        this.name = name;
        this.id = id;
        this.paymentMode = paymentMode;
        this.dues = 0;
        this.isPartofUnion = false;
        this.joiningDate = joiningDate;
    }
    public Employee(String name,int id) {
        this(name,id,"Cash",new Date());
    }
    public Employee() { this("",0,"Cash",new Date()); }

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
    public HourlyPaidEmployee(String name,int id,String paymentMode,Date joiningDate,double hourRate){
        super(name,id,paymentMode,joiningDate);
        this.hourRate = hourRate;
        hoursWorked = new int[5];
        for(int i=0;i<5;i++) hoursWorked[i] = 0;
    }
    public HourlyPaidEmployee(String name,int id,Date joiningDate,double hourRate){
        this(name,id,"Cash",joiningDate,hourRate);
    }
    public HourlyPaidEmployee(String name,int id) { this(name,id,"Cash",new Date(),defaultHourRate); }
    public HourlyPaidEmployee() { this("",0,"Cash",new Date(),0.0); }
    public int[] getHoursWorked() { return hoursWorked; }
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
    public static void main(String[] args) throws ParseException, java.text.ParseException, IOException {
        Scanner scan = new Scanner(System.in);
        SimpleDateFormat myFormat = new SimpleDateFormat("dd/MM/yyyy");

        System.out.println("Control Details : ");
        System.out.println("Enter 1 to Add a new Employee");
        System.out.println("Enter 0 to Exit");

        while(true) {
            int operation = scan.nextInt();
            if(operation == 1){
                addNewEmployee();
            }
            else{
                break;
            }
        }

        scan.close();
    }

    @SuppressWarnings("unchecked")
    public static Long generateId() throws IOException, ParseException {
        JSONObject obj = new JSONObject();
        JSONParser parser = new JSONParser();
        obj = (JSONObject) parser.parse(new FileReader("Employee.json"));
        Long lastusedId = (Long) obj.get("LastIdUsed");
        ++lastusedId;
        obj.put("LastIdUsed", lastusedId);
        try (FileWriter file = new FileWriter("Employee.json")){
            file.write(obj.toString());
            file.flush();
        } catch (Exception e) { e.printStackTrace(); }
        return lastusedId;
    }

    @SuppressWarnings("unchecked")
    public static void addNewEmployee() throws IOException, ParseException, java.text.ParseException {
        Scanner scan = new Scanner(System.in);
        SimpleDateFormat myFormat = new SimpleDateFormat("dd/MM/yyyy");
        System.out.println("Enter the type od Employee : 1 for HourlyPaid and 2 for MonthlyPaid");
        int typeofEmployee = scan.nextInt();
        if(typeofEmployee == 1){
            String name,paymentMode,joiningDate;
            Long id = generateId();
            boolean ispartofUnion;
            Double hourRate;
            System.out.print("Enter the name : "); name = scan.next();
            System.out.print("Enter the joiningDate : "); joiningDate = scan.next();
            System.out.print("Enter the paymentMode : "); paymentMode = scan.next();
            System.out.print("Is Employee part of Union : ");
            String t = scan.next();
            if(t.equals("yes")) ispartofUnion = true;
            else ispartofUnion = false;
            System.out.print("Enter the hourRate : "); hourRate = scan.nextDouble();
            Date d1 = myFormat.parse(joiningDate);
            HourlyPaidEmployee newEmp = new HourlyPaidEmployee(name, Math.toIntExact(id),paymentMode,d1,hourRate);
            JSONObject obj = new JSONObject();
            JSONArray list = new JSONArray();
            JSONParser parser = new JSONParser();
            obj = (JSONObject) parser.parse(new FileReader("Employee.json"));
            list = (JSONArray) obj.get("Employees");
            JSONObject obj1 = new JSONObject();
//            @SuppressWarnings("unchecked");
            obj1.put("name", newEmp.getName());
            obj1.put("id", newEmp.getId());
            obj1.put("dues",0.0);
            obj1.put("isPartofUnion", newEmp.hasUnionMemberShip());
            obj1.put("paymentMode", newEmp.getPaymentMode());
            obj1.put("joiningDate", newEmp.getJoiningDate());
            list.add(obj1);

            obj.put("Employees",list);

            list = (JSONArray) obj.get("HourlyPaidEmployees");
            JSONObject obj2 = new JSONObject();
            obj2.put("id",newEmp.getId());
            obj2.put("hourRate",newEmp.getHourRate());
//            obj2.put("hoursWorked",newEmp.getHoursWorked());
            JSONArray ar = new JSONArray();
            for(int i=0;i<5;i++){
                ar.add(newEmp.getHoursWorked(i));
            }
            obj2.put("hoursWorked",ar);
            list.add(obj2);
            obj.put("HourlyPaidEmployees",list);

            try (FileWriter file = new FileWriter("Employee.json")){
                file.write(obj.toString());
                file.flush();
            } catch (Exception e) { e.printStackTrace(); }

            System.out.print(newEmp.getId());
        }

    }
}
