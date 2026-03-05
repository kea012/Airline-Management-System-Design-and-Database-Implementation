/*
 * Template JAVA User Interface
 * =============================
 *
 * Database Management Systems
 * Department of Computer Science &amp; Engineering
 * University of California - Riverside
 *
 * Target DBMS: 'Postgres'
 *
 */


 import java.sql.DriverManager;
 import java.sql.Connection;
 import java.sql.Statement;
 import java.sql.ResultSet;
 import java.sql.ResultSetMetaData;
 import java.sql.SQLException;
 import java.io.File;
 import java.io.FileReader;
 import java.io.BufferedReader;
 import java.io.InputStreamReader;
 import java.util.List;
 import java.util.ArrayList;
 import java.lang.Math;
 
 /**
  * This class defines a simple embedded SQL utility class that is designed to
  * work with PostgreSQL JDBC drivers.
  *
  */
 public class AirlineManagement {
 
    // reference to physical database connection.
    private Connection _connection = null;
 
    // handling the keyboard inputs through a BufferedReader
    // This variable can be global for convenience.
    static BufferedReader in = new BufferedReader(
                                 new InputStreamReader(System.in));
 
    /**
     * Creates a new instance of AirlineManagement
     *
     * @param hostname the MySQL or PostgreSQL server hostname
     * @param database the name of the database
     * @param username the user name used to login to the database
     * @param password the user login password
     * @throws java.sql.SQLException when failed to make a connection.
     */
    public AirlineManagement(String dbname, String dbport, String user, String passwd) throws SQLException {
 
       System.out.print("Connecting to database...");
       try{
          // constructs the connection URL
          String url = "jdbc:postgresql://localhost:" + dbport + "/" + dbname;
          System.out.println ("Connection URL: " + url + "\n");
 
          // obtain a physical connection
          this._connection = DriverManager.getConnection(url, user, passwd);
          System.out.println("Done");
       }catch (Exception e){
          System.err.println("Error - Unable to Connect to Database: " + e.getMessage() );
          System.out.println("Make sure you started postgres on this machine");
          System.exit(-1);
       }//end catch
    }//end AirlineManagement
 
    /**
     * Method to execute an update SQL statement.  Update SQL instructions
     * includes CREATE, INSERT, UPDATE, DELETE, and DROP.
     *
     * @param sql the input SQL string
     * @throws java.sql.SQLException when update failed
     */
    public void executeUpdate (String sql) throws SQLException {
       // creates a statement object
       Statement stmt = this._connection.createStatement ();
 
       // issues the update instruction
       stmt.executeUpdate (sql);
 
       // close the instruction
       stmt.close ();
    }//end executeUpdate
 
    /**
     * Method to execute an input query SQL instruction (i.e. SELECT).  This
     * method issues the query to the DBMS and outputs the results to
     * standard out.
     *
     * @param query the input query string
     * @return the number of rows returned
     * @throws java.sql.SQLException when failed to execute the query
     */
    public int executeQueryAndPrintResult (String query) throws SQLException {
       // creates a statement object
       Statement stmt = this._connection.createStatement ();
 
       // issues the query instruction
       ResultSet rs = stmt.executeQuery (query);
 
       /*
        ** obtains the metadata object for the returned result set.  The metadata
        ** contains row and column info.
        */
       ResultSetMetaData rsmd = rs.getMetaData ();
       int numCol = rsmd.getColumnCount ();
       int rowCount = 0;
 
       // iterates through the result set and output them to standard out.
       boolean outputHeader = true;
       while (rs.next()){
        if(outputHeader){
          for(int i = 1; i <= numCol; i++){
          System.out.print(rsmd.getColumnName(i) + "\t");
          }
          System.out.println();
          outputHeader = false;
        }
          for (int i=1; i<=numCol; ++i)
             System.out.print (rs.getString (i) + "\t");
          System.out.println ();
          ++rowCount;
       }//end while
       stmt.close();
       return rowCount;
    }//end executeQuery
 
    /**
     * Method to execute an input query SQL instruction (i.e. SELECT).  This
     * method issues the query to the DBMS and returns the results as
     * a list of records. Each record in turn is a list of attribute values
     *
     * @param query the input query string
     * @return the query result as a list of records
     * @throws java.sql.SQLException when failed to execute the query
     */
    public List<List<String>> executeQueryAndReturnResult (String query) throws SQLException {
       // creates a statement object
       Statement stmt = this._connection.createStatement ();
 
       // issues the query instruction
       ResultSet rs = stmt.executeQuery (query);
 
       /*
        ** obtains the metadata object for the returned result set.  The metadata
        ** contains row and column info.
        */
       ResultSetMetaData rsmd = rs.getMetaData ();
       int numCol = rsmd.getColumnCount ();
       int rowCount = 0;
 
       // iterates through the result set and saves the data returned by the query.
       boolean outputHeader = false;
       List<List<String>> result  = new ArrayList<List<String>>();
       while (rs.next()){
         List<String> record = new ArrayList<String>();
       for (int i=1; i<=numCol; ++i)
          record.add(rs.getString (i));
         result.add(record);
       }//end while
       stmt.close ();
       return result;
    }//end executeQueryAndReturnResult
 
    /**
     * Method to execute an input query SQL instruction (i.e. SELECT).  This
     * method issues the query to the DBMS and returns the number of results
     *
     * @param query the input query string
     * @return the number of rows returned
     * @throws java.sql.SQLException when failed to execute the query
     */
    public int executeQuery (String query) throws SQLException {
        // creates a statement object
        Statement stmt = this._connection.createStatement ();
 
        // issues the query instruction
        ResultSet rs = stmt.executeQuery (query);
 
        int rowCount = 0;
 
        // iterates through the result set and count number of results.
        while (rs.next()){
           rowCount++;
        }//end while
        stmt.close ();
        return rowCount;
    }
 
    /**
     * Method to fetch the last value from sequence. This
     * method issues the query to the DBMS and returns the current
     * value of sequence used for autogenerated keys
     *
     * @param sequence name of the DB sequence
     * @return current value of a sequence
     * @throws java.sql.SQLException when failed to execute the query
     */
    public int getCurrSeqVal(String sequence) throws SQLException {
       Statement stmt = this._connection.createStatement ();
 
       ResultSet rs = stmt.executeQuery (String.format("Select currval('%s')", sequence));
       if (rs.next())
          return rs.getInt(1);
       return -1;
    }
 
    /**
     * Needed for the CreateUser query
     */
    public int executeCountQuery(String query) throws SQLException {
       // creates a statement object
       Statement stmt = this._connection.createStatement ();
 
       // issues the query instruction
       ResultSet rs = stmt.executeQuery (query);
 
       int val = 0;
       if (rs.next()) {
         val = rs.getInt(1);
       }
       
       stmt.close();
       return val;
    }
 
    /**
     * Needed for the CreateUser function to get the most recent id
     */
    public String getValueQuery(String query) throws SQLException {
       // creates a statement object
       Statement stmt = this._connection.createStatement ();
 
       // issues the query instruction
       ResultSet rs = stmt.executeQuery (query);
 
       String val = null;
       if (rs.next()) {
         val = rs.getString(1);
       }
       
       stmt.close();
       return val;
    }
 
    /**
     * Method to close the physical connection if it is open.
     */
    public void cleanup(){
       try{
          if (this._connection != null){
             this._connection.close ();
          }//end if
       }catch (SQLException e){
          // ignored.
       }//end try
    }//end cleanup
 
    /**
     * The main execution method
     *
     * @param args the command line arguments this inclues the <mysql|pgsql> <login file>
     */
    public static void main (String[] args) {
       if (args.length != 3) {
          System.err.println (
             "Usage: " +
             "java [-classpath <classpath>] " +
             AirlineManagement.class.getName () +
             " <dbname> <port> <user>");
          return;
       }//end if
 
       Greeting();
       AirlineManagement esql = null;
       try{
          // use postgres JDBC driver.
          Class.forName ("org.postgresql.Driver").newInstance ();
          // instantiate the AirlineManagement object and creates a physical
          // connection.
          String dbname = args[0];
          String dbport = args[1];
          String user = args[2];
          esql = new AirlineManagement (dbname, dbport, user, "");
 
          boolean keepon = true;
          while(keepon) {
             // These are sample SQL statements
             System.out.println("\nMAIN MENU");
             System.out.println("---------");
             System.out.println("1. Create user");
             System.out.println("2. Log in");
             System.out.println("9. < EXIT");
             String authorisedUser = null;
             switch (readChoice()){
                case 1: CreateUser(esql); break;
                case 2: authorisedUser = LogIn(esql); break;
                case 9: keepon = false; break;
                default : System.out.println("Unrecognized choice!"); break;
             }//end switch
             if (authorisedUser != null) {
               boolean usermenu = true;
               while(usermenu) {
                 System.out.println("\nMAIN MENU");
                 System.out.println("---------");
 
                 //**the following functionalities should only be able to be used by Management**
                 System.out.println("1. View Flights");
                 System.out.println("2. View Flight Seats");
                 System.out.println("3. View Flight Status");
                 System.out.println("4. View Flights of the Day");  
                 System.out.println("5. View Full Order ID History");
                 System.out.println("6. View Full Customer Information");
                 System.out.println("7. View Full Plane Information");
                 System.out.println("8. View Repairs Made by a Maintenance Technician");
                 System.out.println("9. View Full Plane Repair Information");
                 System.out.println("10. View Full Plane Information");
                 System.out.println("11. Mark Reservations As Flown"); // Kenny Implemented a function to mark reservations as flown
 
                 //**the following functionalities should only be able to be used by customers**
                 System.out.println("12. Search Flights");
                 System.out.println("13. Search Ticket Cost");
                 System.out.println("14. Search Airplane Type");
                 System.out.println("15. Make Reservation Or Join Waitlist");
 
                 //**the following functionalities should ony be able to be used by Pilots**
                 System.out.println("16. Maintenace Request");
                 //System.out.println(".........................");
                 //System.out.println(".........................");
 
                //**the following functionalities should ony be able to be used by Technicians**
                 System.out.println("17. View Plane Repairs");
                 System.out.println("18. View Pilot Requests");
                 System.out.println("19. Complete Repair");
 
                 System.out.println("20. Log out");
                 switch (readChoice()){
                    case 1: feature1(esql, authorisedUser); break;
                    case 2: feature2(esql, authorisedUser); break;
                    case 3: feature3(esql, authorisedUser); break;
                    case 4: feature4(esql, authorisedUser); break;
                    case 5: feature5(esql, authorisedUser); break;
                    case 6: feature6(esql, authorisedUser); break;
                    case 7: feature7(esql, authorisedUser); break;
                    case 8: feature8(esql, authorisedUser); break;
                    case 9: feature9(esql, authorisedUser); break;
                    case 10: feature10(esql, authorisedUser); break;
                    case 11: feature11(esql, authorisedUser); break;
                    case 12: feature12(esql, authorisedUser); break;
                    case 13: feature13(esql, authorisedUser); break;
                    case 14: feature14(esql, authorisedUser); break;
                    case 15: feature15(esql, authorisedUser); break;
                    case 16: makeRequest(esql, authorisedUser); break;
                    case 17: viewPlaneRepairs(esql, authorisedUser); break;
                    case 18: viewPilotRequests(esql, authorisedUser); break;
                    case 19: completeRepair(esql, authorisedUser); break;
 
 
 
 
                    case 20: usermenu = false; break;
                    default : System.out.println("Unrecognized choice!"); break;
                 }
               }
             }
          }//end while
       }catch(Exception e) {
          System.err.println (e.getMessage ());
       }finally{
          // make sure to cleanup the created table and close the connection.
          try{
             if(esql != null) {
                System.out.print("Disconnecting from database...");
                esql.cleanup ();
                System.out.println("Done\n\nBye !");
             }//end if
          }catch (Exception e) {
             // ignored.
          }//end try
       }//end try
    }//end main
 
    public static void Greeting(){
       System.out.println(
          "\n\n*******************************************************\n" +
          "              User Interface      	               \n" +
          "*******************************************************\n");
    }//end Greeting
 
    /*
     * Reads the users choice given from the keyboard
     * @int
     **/
    public static int readChoice() {
       int input;
       // returns only if a correct value is given.
       do {
          System.out.print("Please make your choice: ");
          try { // read the integer, parse it and break.
             input = Integer.parseInt(in.readLine());
             break;
          }catch (Exception e) {
             System.out.println("Your input is invalid!");
             continue;
          }//end try
       }while (true);
       return input;
    }//end readChoice
 
    /*
     * Creates a new user
     **/
    public static void CreateUser(AirlineManagement esql){
       System.out.println("\nCREATE NEW USER");
       System.out.println("---------------");
 
       String username = null;
       String password = null;
       String role = null;
       boolean keepon = true;
       while(keepon) {
          try{
             System.out.print("Enter username: ");
             username = in.readLine();
 
             System.out.print("Enter password: ");
             password = in.readLine();
             System.out.println();
 
             System.out.print("What is your role?\n1. Management\n2. Customer (default)\n3. Pilot\n4. Technician\nOption: ");
 
             String option = in.readLine();
             int optionInt = Integer.parseInt(option);
             if (optionInt == 1) {
                role = "Management";
             }else if (optionInt == 2) {
                role = "Customer";
             }else if (optionInt == 3) {
                role = "Pilot";
             } else if (optionInt == 4) {
                role = "Technician";
             } else{
                role = "Customer";
             }
 
             // Checks if user is a staff member or customer
             if (optionInt == 1 || optionInt == 3 || optionInt == 4) {
                 String staffCredQuery = "CREATE TABLE IF NOT EXISTS StaffCredentials (username VARCHAR(50) PRIMARY KEY, password VARCHAR(255) NOT NULL, role VARCHAR(50), staffID TEXT NOT NULL)";
                 esql.executeUpdate(staffCredQuery);
 
                 //Check if username already exists in StaffCredentials table
                 String checkQuery = "SELECT COUNT(*) FROM StaffCredentials S WHERE S.username = ";
                 username = "'" + username + "'";
                 checkQuery += username;
 
                 int numUsers = esql.executeCountQuery(checkQuery);
                 if (numUsers != 0) {
                     System.out.println("\nError - Username " + username + " is already taken, try again.\n");
                 } else{
                   System.out.println();
                   System.out.println("Creating user...");
                   keepon = false;
                 }
             } else{
                //System.out.println("In Customers create section");
                String custCredQuery = "CREATE TABLE IF NOT EXISTS CustomerCredentials (username VARCHAR(50) PRIMARY KEY, password VARCHAR(255) NOT NULL, role VARCHAR(50), custID INTEGER NOT NULL)";
                esql.executeUpdate(custCredQuery);
 
                //Check if username already exists in CustomerCredentials table
                String checkQuery = "SELECT COUNT(*) FROM CustomerCredentials C WHERE C.username = ";
                username = "'" + username + "'";
                checkQuery += username;
 
                int numUsers = esql.executeCountQuery(checkQuery);
                if (numUsers != 0) {
                   System.out.println("\nError - Username " + username + " is already taken, try again.\n");
                } else{
                   //System.out.println();
                   //System.out.println("Creating user...");
                   keepon = false;
                }
             }
          }catch(Exception e){
             System.err.println(e.getMessage());
          }
       }
       try{
         //insert Staff credentials
         if (role.equals("Management") || role.equals("Pilot") || role.equals("Technician")) {
             String staffid = null;
             System.out.print("Enter Full Name: ");
             String name = in.readLine();
             if (role.equals("Pilot")) {
                // get new staffID from Pilots table
                String query1 = "SELECT MAX(P.PilotID) FROM Pilot P";
                String mostRecentPilotID = esql.getValueQuery(query1);
 
                String numPart = mostRecentPilotID.substring(1);
                int mostRecentNum = Integer.parseInt(numPart);
 
                mostRecentNum += 1;
 
                staffid = String.format("P%03d", mostRecentNum);
 
                //Insert new Pilot into Pilot table
                String insertQuery = "INSERT INTO Pilot (PilotID, Name) VALUES (";
                staffid = "'" + staffid + "'";
                name = "'" + name + "'";
                insertQuery += staffid + ", " + name + ")";
 
                esql.executeUpdate(insertQuery);
             } else if (role.equals("Technician")) {
                // get new TechnicianID from Technician table
                String query1 = "SELECT MAX(T.TechnicianID) FROM Technician T";
                String mostRecentTechID = esql.getValueQuery(query1);
 
                String numPart = mostRecentTechID.substring(1);
                int mostRecentNum = Integer.parseInt(numPart);
 
                mostRecentNum += 1;
 
                staffid = String.format("T%03d", mostRecentNum);
 
                //insert new Technician into Technician table
                String insertQuery = "INSERT INTO Technician (TechnicianID, Name) VALUES (";
                staffid = "'" + staffid + "'";
                name = "'" + name + "'";
                insertQuery += staffid + ", " + name + ")";
 
                esql.executeUpdate(insertQuery);
             } else {
                // create Management table if needed (should only be executed once)
                String query = "CREATE TABLE IF NOT EXISTS Management (ManagementID TEXT PRIMARY KEY, Name TEXT)";
                esql.executeUpdate(query);
 
                // get new ManagementID from Management table
                String query1 = "SELECT MAX(M.ManagementID) FROM Management M";
                String mostRecentManageID = esql.getValueQuery(query1);
 
                if (mostRecentManageID == null) {
                   staffid = "M001";
                } else{
                   String numPart = mostRecentManageID.substring(1);
                   int mostRecentNum = Integer.parseInt(numPart);
 
                   mostRecentNum += 1;
 
                   staffid = String.format("M%03d", mostRecentNum);
                }
                //insert new Management into Management table
                String insertQuery = "INSERT INTO Management (ManagementID, Name) VALUES (";
                staffid = "'" + staffid + "'";
                name = "'" + name + "'";
                insertQuery += staffid + ", " + name + ")";
 
                esql.executeUpdate(insertQuery);
             }
             //insert the new data to the StaffCredentials table
             String insertQuery = "INSERT INTO StaffCredentials (username, password, role, staffID) VALUES (";
             password = "'" + password + "'";
             role = "'" + role + "'";
 
             insertQuery += username + ", " + password + ", " + role + ", " + staffid + ")";
 
             esql.executeUpdate(insertQuery);
         } else {
             System.out.println("\nCustomer Information");
             System.out.println("--------------------");
 
             //read first name, last name, gender, DOB, Address, Phone #, and ZipCode from customer
             System.out.print("Enter First Name: ");
             String firstname = in.readLine();
 
             System.out.print("Enter Last Name: ");
             String lastname = in.readLine();
 
             System.out.print("Enter Gender: ");
             String gender = in.readLine();
 
             System.out.print("Enter Date of Birth (YYYY-MM-DD): ");
             String dob = in.readLine();
 
             System.out.print("Enter Address: ");
             String address = in.readLine();
             address = "\"" + address + "\"";
 
             System.out.print("Enter Phone Number(XXX.XXX.XXXX): ");
             String phoneNum = in.readLine();
 
             System.out.print("Enter Zip Code: ");
             String zip = in.readLine();
 
             // get new CustomerID from Customer table
             String query1 = "SELECT MAX(C.CustomerID) FROM Customer C";
             int mostRecentCustID = esql.executeCountQuery(query1);
 
             mostRecentCustID += 1;
 
             // insert data into Customer table
             //System.out.println("In the INSERT Customer table section");
             String insertQuery = "INSERT INTO Customer (CustomerID, FirstName, LastName, Gender, DOB, Address, Phone, Zip) VALUES (";
             firstname = "'" + firstname + "'";
             lastname = "'" + lastname + "'";
             gender = "'" + gender + "'";
             dob = "'" + dob + "'";
             address = "'" + address + "'";
             phoneNum = "'" + phoneNum + "'";
             zip = "'" + zip + "'";
 
             insertQuery += mostRecentCustID + ", " + firstname + ", " + lastname + ", " + gender + ", " + dob + ", " + address + ", ";
             insertQuery += phoneNum + ", " + zip + ")";
 
             esql.executeUpdate(insertQuery);
 
             // insert the new data into the CustomerCredentials table
             //System.out.println("In the INSERT CustomerCredentials table section");
             insertQuery = "INSERT INTO CustomerCredentials (username, password, role, custID) VALUES (";
             password = "'" + password + "'";
             role = "'" + role + "'";
 
             insertQuery += username + ", " + password + ", " + role + ", " + mostRecentCustID + ")";
 
             esql.executeUpdate(insertQuery);
         }
       }catch(Exception e){
          System.err.println("User Could not be Created - " + e.getMessage());
       }
    }//end CreateUser
 
 
    /*
     * Check log in credentials for an existing user
     * @return User login or null is the user does not exist
     **/
    // How do I check if a User is in the database??
    public static String LogIn(AirlineManagement esql){
       String result = null;
       System.out.println("\nLog In");
       System.out.println("------");
 
       String username = null;
       String password = null;
       String role = null;
       try{
          System.out.print("Enter username: ");
          username = in.readLine();
 
          System.out.print("Enter password: ");
          password = in.readLine();
 
          System.out.println();
          System.out.print("What is your role?\n1. Management\n2. Customer (default)\n3. Pilot\n4. Technician\nOption: ");
 
          String option = in.readLine();
          int optionInt = Integer.parseInt(option);
          if (optionInt == 1) {
             role = "Management";
          }else if (optionInt == 2) {
             role = "Customer";
          }else if (optionInt == 3) {
             role = "Pilot";
          } else if (optionInt == 4) {
             role = "Technician";
          } else{
             role = "Customer";
          }
 
          // Check if credentials are valid
          // if role == Customer, then check the CustomerCredentials table. if not check the StaffCredentials table
          String table = null;
          if (role.equals("Customer")) {
             table = "CustomerCredentials";
          } else{
             table = "StaffCredentials";
          }
          String checkQuery = "SELECT T.username, T.password FROM " + table + " T WHERE T.username = '";
          checkQuery += username + "'";
 
          checkQuery += " AND T.password = '";
          checkQuery += password + "'" + " AND T.role = '" + role + "'";
 
          int numUsers = esql.executeQuery(checkQuery);
          if (numUsers == 0) {
             System.out.println("No User Found.");
          }else if (numUsers == 1) {
             String lookUpName = null;
             //check what role they are to determine what table to look up ID from
             if(optionInt == 1) {
                //they're in Management
                lookUpName = "SELECT M.Name FROM Management M, StaffCredentials S WHERE S.username = ";
                lookUpName += "'" + username + "' AND M.ManagementID = S.staffID";
                lookUpName = esql.getValueQuery(lookUpName);
 
                lookUpName = lookUpName.split(" ")[0];
             } else if(optionInt == 3) {
                //they're a Pilot
                lookUpName = "SELECT P.Name FROM Pilot P, StaffCredentials S WHERE S.username = ";
                lookUpName += "'" + username + "' AND P.PilotID = S.staffID";
                lookUpName = esql.getValueQuery(lookUpName);
 
                lookUpName = lookUpName.split(" ")[0];
             } else if(optionInt == 4) {
                //they're a Technician
                lookUpName = "SELECT T.Name FROM Technician T, StaffCredentials S WHERE S.username = ";
                lookUpName += "'" + username + "' AND T.TechnicianID = S.staffID";
                lookUpName = esql.getValueQuery(lookUpName);
 
                lookUpName = lookUpName.split(" ")[0];
             } else {
                //they're a Customer
                lookUpName = "SELECT C.FirstName FROM Customer C, CustomerCredentials S WHERE S.username = ";
                lookUpName += "'" + username + "' AND C.CustomerID = S.custID";
                lookUpName = esql.getValueQuery(lookUpName);
             }
             System.out.println("Welcome " + lookUpName + "!");
             result = username + "," + password + "," + role;
          }
       }catch(Exception e){
          System.err.println(e.getMessage());
       }
       return result;
    }//end
 
 // Rest of the functions definition go in here
 
    // Management queries
    public static void feature1(AirlineManagement esql, String authorisedUser) {
       // first check if the user is a Managment person, if not kick them back to the main menu
       String[] credentials = authorisedUser.split(",");
       String role = credentials[2];
 
       if (!role.equals("Management")) {
          System.out.println("Sorry you can't complete this action as you are not part of Management.");
          return;
       }
 
       System.out.println("\nFlight's Schedule");
       System.out.println("-----------------");
       try{
          System.out.print("Enter Flight Number: ");
          String flightNum = in.readLine();
 
          String query = "SELECT F.FlightNumber AS Flight_Number, I.FlightDate AS Date, S.DayOfWeek AS Day, S.DepartureTime AS Dep_Time, S.ArrivalTime AS Arr_Time, F.DepartureCity AS Dep_City, F.ArrivalCity AS Arr_City FROM Flight F, FlightInstance I, Schedule S WHERE F.FlightNumber = I.FlightNumber AND F.FlightNumber = S.FlightNumber AND F.FlightNumber = '" + flightNum;
          query += "' AND I.FlightDate >= CURRENT_DATE AND I.FlightDate < CURRENT_DATE + INTERVAL '7 days' AND S.DayOfWeek = TRIM(TO_CHAR(I.FlightDate, 'Day')) ORDER BY I.FlightDate ASC";
 
          long start = System.currentTimeMillis();
 
          int rowCount = esql.executeQueryAndPrintResult(query);
          
          long end = System.currentTimeMillis();
          System.out.println("Execution Time (ms): " + (end - start));
 
          System.out.println ("total row(s): " + rowCount);
       }catch(Exception e){
          System.err.println (e.getMessage());
       }
    }//end feature1
 
    public static void feature2(AirlineManagement esql, String authorisedUser) {
       // first check if the user is a Managment person, if not kick them back to the main menu
       String[] credentials = authorisedUser.split(",");
       String role = credentials[2];
 
       if (!role.equals("Management")) {
          System.out.println("Sorry you can't complete this action as you are not part of Management.");
          return;
       }
 
       // need to read flight number and date from user
       System.out.println("\nFlight's Seats");
       System.out.println("--------------");
       try{
          System.out.print("Enter Flight Number: ");
          String flightNum = in.readLine();
          System.out.print("Enter Date (YYYY-MM-DD): ");
          String date = in.readLine();
 
          String query = "SELECT I.FlightNumber AS Flight_Number, I.FlightDate AS Date, I.SeatsTotal - I.SeatsSold AS Seats_Avail, I.SeatsSold AS Seats_Sold";
          query += " FROM FlightInstance I WHERE I.FlightNumber = '" + flightNum + "' AND I.FlightDate = '" + date + "'";
 
          long start = System.currentTimeMillis();
          esql.executeQueryAndPrintResult(query);
 
          long end = System.currentTimeMillis();
    
          System.out.println("Execution Time (ms): " + (end - start));
 
       }catch(Exception e){
          System.err.println (e.getMessage());
       }
    }
    public static void feature3(AirlineManagement esql, String authorisedUser) {
       // first check if the user is a Managment person, if not kick them back to the main menu
       String[] credentials = authorisedUser.split(",");
       String role = credentials[2];
 
       if (!role.equals("Management")) {
          System.out.println("Sorry you can't complete this action as you are not part of Management.");
          return;
       }
 
       // need to read flight number and date from user
       System.out.println("\nFlight Status");
       System.out.println("-------------");
       try{
          System.out.print("Enter Flight Number: ");
          String flightNum = in.readLine();
          System.out.print("Enter Date (YYYY-MM-DD): ");
          String date = in.readLine();
 
          String query = "SELECT I.FlightNumber AS Flight_Number, I.FlightDate AS Date, CASE WHEN I.DepartedOnTime IS TRUE THEN 'Yes' WHEN I.DepartedOnTime IS FALSE THEN 'No' ELSE 'Unknown' END AS Departed_On_Time, ";
          query += "CASE WHEN I.ArrivedOnTime IS TRUE THEN 'Yes' WHEN I.ArrivedOnTime IS FALSE THEN 'No' ELSE 'Unknown' END AS Arrived_On_Time ";
          query += "FROM FlightInstance I WHERE I.FlightNumber = '" + flightNum + "' AND I.FlightDate = '" + date + "'";
 
          long start = System.currentTimeMillis();
          esql.executeQueryAndPrintResult(query);
 
          long end = System.currentTimeMillis();
    
          System.out.println("Execution Time (ms): " + (end - start));
          
       }catch(Exception e){
          System.err.println (e.getMessage());
       }
    }
    public static void feature4(AirlineManagement esql, String authorisedUser) {
       // first check if the user is a Managment person, if not kick them back to the main menu
       String[] credentials = authorisedUser.split(",");
       String role = credentials[2];
 
       if (!role.equals("Management")) {
          System.out.println("Sorry you can't complete this action as you are not part of Management.");
          return;
       }
 
       // read date from user
       System.out.println("\nFlight Schedule");
       System.out.println("---------------");
       try{
          System.out.print("Enter Date (YYYY-MM-DD): ");
          String date = in.readLine();
 
          String query = "SELECT I.FlightNumber AS Flight_Number, I.FlightDate AS Date, I.TicketCost AS Ticket_Cost ";
          query += "FROM FlightInstance I WHERE I.FlightDate = '" + date + "'";
 
          System.out.print("How would you like to sort the flights?\n1. Flight Number (default)\n2. Price\nOption: ");
          String option = in.readLine();
          int optionInt = Integer.parseInt(option);
          if (optionInt == 2) {
             query += " ORDER BY I.TicketCost ASC";
          }
 
          long start = System.currentTimeMillis();
 
          int numFlights = esql.executeQueryAndPrintResult(query);
 
          long end = System.currentTimeMillis();
    
          System.out.println("Execution Time (ms): " + (end - start));
 
          System.out.println ("total flight(s): " + numFlights);
       }catch(Exception e){
          System.err.println (e.getMessage());
       }
    }
    public static void feature5(AirlineManagement esql, String authorisedUser) {
       // first check if the user is a Managment person, if not kick them back to the main menu
       String[] credentials = authorisedUser.split(",");
       String role = credentials[2];
 
       if (!role.equals("Management")) {
          System.out.println("Sorry you can't complete this action as you are not part of Management.");
          return;
       }
 
       // read flight number and date from user
       System.out.println("\nOrder ID History");
       System.out.println("----------------");
       try{
          System.out.print("Enter Flight Number: ");
          String flightNum = in.readLine();
          System.out.print("Enter Date (YYYY-MM-DD): ");
          String date = in.readLine();
 
          String query = "SELECT DISTINCT I.FlightNumber AS Flight_Number, I.FlightDate AS Date, R.CustomerID AS Customer_ID, R.Status FROM FlightInstance I, Reservation R";
          query += " WHERE I.FlightInstanceID = R.FlightInstanceID AND I.FlightNumber = '" + flightNum + "' AND I.FlightDate = '" + date + "' ORDER BY R.Status DESC";
 
          long start = System.currentTimeMillis();
          
          List<List<String>> results = esql.executeQueryAndReturnResult(query);
 
          long end = System.currentTimeMillis();
    
          System.out.println("Execution Time (ms): " + (end - start));
 
          List<List<Integer>> formatted_results = new ArrayList<>();
          for (int i = 0; i < 3; i++) {
             formatted_results.add(new ArrayList<>());
          }
 
          for (List<String> row : results) {
             if ("waitlist".equals(row.get(3))) {
                formatted_results.get(0).add(Integer.parseInt(row.get(2)));
             }else if ("reserved".equals(row.get(3))) {
                formatted_results.get(1).add(Integer.parseInt(row.get(2)));
             }else {
                formatted_results.get(2).add(Integer.parseInt(row.get(2)));
             }
          }
 
          List<String> prompts = new ArrayList<>();
          prompts.add("People who waitlisted: ");
          prompts.add("People who made reservations: ");
          prompts.add("People who actually flew: ");
 
          int i = 0;
          int numPass = 0;
          for (List<Integer> group : formatted_results) {
             System.out.print(prompts.get(i));
             System.out.println(group);
             numPass += group.size();
             i += 1;
          }
          System.out.println ("total passenger(s): " + numPass);
       }catch(Exception e){
          System.err.println (e.getMessage());
       }
    }
 
    // Customer Queries
    public static void feature6(AirlineManagement esql, String authorisedUser) {
        // first check if the user is a Managment person, if not kick them back to the main menu
        String[] credentials = authorisedUser.split(",");
        String role = credentials[2];
  
        if (!role.equals("Management")) {
           System.out.println("Sorry you can't complete this action as you are not part of Management.");
           return;
        }
  
        // read flight number and date from user
        System.out.println("\nTraveler Information");
        System.out.println("--------------------");
       try {
          String query = "SELECT c.FirstName, c.LastName, c.Gender, c.DOB, c.Address, c.Phone, c.Zip " +
                         "FROM Customer c NATURAL JOIN Reservation r " +
                         "WHERE r.ReservationID = ";
    
          System.out.print("Enter reservation number: ");
          String input = in.readLine();
          query += "'" + input + "'";
    
          long start = System.currentTimeMillis();
    
          int rowCount = esql.executeQueryAndPrintResult(query);
    
          long end = System.currentTimeMillis();
    
          System.out.println("Time " + (end - start) + " ms");
    
          System.out.println ("Total Customer(s): " + rowCount);
       }catch(Exception e){
          System.err.println (e.getMessage());
       }
    }
 
    public static void feature7(AirlineManagement esql, String authorisedUser) {
        // first check if the user is a Managment person, if not kick them back to the main menu
        String[] credentials = authorisedUser.split(",");
        String role = credentials[2];
  
        if (!role.equals("Management")) {
           System.out.println("Sorry you can't complete this action as you are not part of Management.");
           return;
        }
  
        // read plane id from airline manager to get information about planes
        System.out.println("\nPlane Information");
        System.out.println("-----------------");
       try {
          String query = "SELECT p.Make, p.Model, p.Year, p.LastRepairDate " +
                         "FROM Plane p " +
                         "WHERE p.PlaneID = ";
          
          //doesn't need index, primary key in where clause
 
          //prompt says plane number we interpret as plane id 
          System.out.print("Enter Plane ID: ");
          String input = in.readLine();
          query += "'" + input + "'";
 
          //keep track of query runtime
          long start = System.currentTimeMillis();
    
          int rowCount = esql.executeQueryAndPrintResult(query);
    
          long end = System.currentTimeMillis();
    
          System.out.println("Execution Time (ms): " + (end - start));
    
          System.out.println ("Total Row(s): " + rowCount);
       }catch(Exception e){
          System.err.println (e.getMessage());
       }
    }
 
    public static void feature8(AirlineManagement esql, String authorisedUser) {
       // first check if the user is a Managment person, if not kick them back to the main menu
       String[] credentials = authorisedUser.split(",");
       String role = credentials[2];
 
       if (!role.equals("Management")) {
          System.out.println("Sorry you can't complete this action as you are not part of Management.");
          return;
       }
 
       // read technician id from airline manager to get repairs done by that technician 
       System.out.println("\nRepairs Made By Maintenance Technicians");
       System.out.println("---------------------------------------");
       try {
         String query = "SELECT r.PlaneID, r.RepairCode, r.RepairDate, R.TechnicianID " +
                        "FROM Repair r " +
                        "WHERE r.TechnicianID = ";
 
       // create an indx for repair technician id 
   
         System.out.print("Enter Technician ID: ");
         String input1 = in.readLine();
         query += "'" + input1 + "'";
 
         // keep track of query run time 
   
         long start = System.currentTimeMillis();
   
         int rowCount = esql.executeQueryAndPrintResult(query);
   
         long end = System.currentTimeMillis();
   
         System.out.println("Execution Time (ms): " + (end - start));
    
         System.out.println ("Total Repair(s): " + rowCount);
       }catch(Exception e){
         System.err.println (e.getMessage());
       }
    }
 
    public static void feature9(AirlineManagement esql, String authorisedUser) {
        // first check if the user is a Managment person, if not kick them back to the main menu
        String[] credentials = authorisedUser.split(",");
        String role = credentials[2];
  
        if (!role.equals("Management")) {
           System.out.println("Sorry you can't complete this action as you are not part of Management.");
           return;
        }
  
        // read plane id, start date, end date to get plane repair information in date range
        System.out.println("\nPlane Repair Information");
        System.out.println("------------------------");
       try {
          String query = "SELECT r.RepairCode, r.RepairDate " +
                         "FROM Repair r " +
                         "WHERE r.PlaneID = ";
 
          // create index for repair plane id 
    
          System.out.print("Enter Plane ID: ");
          String input1 = in.readLine();
          query += "'" + input1 + "'";
    
          System.out.print("Enter Start Date (YYYY-MM-DD): ");
          String input2 = in.readLine();
          query += " AND r.RepairDate >= '" + input2 + "'";
    
          System.out.print("Enter End Date (YYYY-MM-DD): ");
          String input3 = in.readLine();
          query += " AND r.RepairDate <= '" + input3 + "'";
 
          // created index for repair repairdata and plane id 
 
          // keep track of run time 
 
          long start = System.currentTimeMillis();
    
          int rowCount = esql.executeQueryAndPrintResult(query);
    
          long end = System.currentTimeMillis();
    
          System.out.println("Execution Time (ms): " + (end - start));
    
          System.out.println ("Total Repair(s): " + rowCount);
       }catch(Exception e){
          System.err.println (e.getMessage());
       }
    }
    
    public static void feature10(AirlineManagement esql, String authorisedUser) {
       // first check if the user is a Managment person, if not kick them back to the main menu
       String[] credentials = authorisedUser.split(",");
       String role = credentials[2];
  
       if (!role.equals("Management")) {
          System.out.println("Sorry you can't complete this action as you are not part of Management.");
          return;
       }
  
       // read flight number and date range from user get sum of seatsold for that flight number get sum of seats left
       // also get number days departed on time and number of days arrived on time 
       System.out.println("\nFlight Statistics");
       System.out.println("-----------------");
       try {
          String query = "SELECT SUM(fi.SeatsSold) AS Seats_Sold, SUM(fi.SeatsTotal - fi.SeatsSold) AS Seats_Unsold, SUM(fi.DepartedOnTime::int) AS Days_Departed, SUM(fi.ArrivedOnTime::int) AS Days_Arrived " +
                         "FROM FlightInstance fi " +
                         "WHERE fi.FlightNumber = ";
    
          System.out.print("Enter Flight Number: ");
          String input1 = in.readLine();
          query += "'" + input1 + "'";
    
          System.out.print("Enter Start Date (YYYY-MM-DD): ");
          String input2 = in.readLine();
          query += " AND fi.FlightDate >= '" + input2 + "'";
    
          System.out.print("Enter End date (YYYY-MM-DD): ");
          String input3 = in.readLine();
          query += " AND fi.FlightDate <= '" + input3 + "'";
 
          // don't need to make new indexes they already exist
 
          // keep track of run time
          long start = System.currentTimeMillis();
    
          int rowCount = esql.executeQueryAndPrintResult(query);
    
          long end = System.currentTimeMillis();
    
          System.out.println("Execution Time (ms): " + (end - start));
    
          System.out.println ("Total Row(s): " + rowCount);
       }catch(Exception e){
          System.err.println (e.getMessage());
       }
    }
 
    public static void feature11(AirlineManagement esql, String authorisedUser) {
       // first check if the user is a Managment person, if not kick them back to the main menu
       String[] credentials = authorisedUser.split(",");
       String role = credentials[2];
  
       if (!role.equals("Management")) {
          System.out.println("Sorry you can't complete this action as you are not part of Management.");
          return;
       }
  
       // read flight number and date from user
       System.out.println("\nMark Reservations As Flown");
       System.out.println("--------------------------");
       try {
 
          System.out.print("Enter Date After Flights Took Place (YYYY-MM-DD): ");
          String input = in.readLine();
 
          String query = "SELECT fi.FlightInstanceID " +
                         "FROM FlightInstance fi " +
                         "WHERE fi.FlightDate < '";
          
          query += input + "' AND fi.DepartedOnTime IS NOT NULL AND fi.ArrivedOnTime IS NOT NULL";
 
          // these already have indexes
 
          long start = System.currentTimeMillis();
 
          List<List<String>> flights = esql.executeQueryAndReturnResult(query);
 
          long end = System.currentTimeMillis();
 
          long time = end - start;
 
          long start1 = 0;
          long end1 = 0;
          long time_loop = 0;
 
          for (int i = 0; i < flights.size(); ++i) {
 
             String query1 = "UPDATE Reservation " +
                             "SET Status = 'flown' " +
                             "WHERE FlightInstanceID = '" + flights.get(i).get(0) + "' AND Status = 'reserved'";
             
             // Already has indexes
             start1 = System.currentTimeMillis();
             
             esql.executeUpdate(query1);
 
             end1 = System.currentTimeMillis();
 
             time_loop = time_loop + (end1 - start1);
          }
 
          System.out.println("Marked all flight reservations before date as flown");
          System.out.println("Execution Time (ms): " + (time + time_loop));
          
       }catch(Exception e){
          System.err.println (e.getMessage());
       }
    }
 
    // customer
    public static void feature12(AirlineManagement esql, String authorisedUser) {
       // first check if the user is a Customer person, if not kick them back to the main menu
       String[] credentials = authorisedUser.split(",");
       String role = credentials[2];
  
       if (!role.equals("Customer")) {
          System.out.println("Sorry you can't complete this action as you are not a Customer.");
          return;
       }
  
       // read departure city arrival city flight date and get the flight statistics
       System.out.println("\nCustomer Flight Information");
       System.out.println("---------------------------");
       try {
          String query = "SELECT s.DepartureTime, s.ArrivalTime, fi.NumOfStops, (SUM(fi.DepartedOnTime::int)::float + SUM(fi.ArrivedOnTime::int)::float) / (2 * COUNT(*)) AS OnTimeRecordPercentage " +
                         "FROM Flight f NATURAL JOIN FlightInstance fi NATURAL JOIN Schedule s " +
                         "WHERE f.DepartureCity = ";
    
          System.out.print("Enter Flight Departure City: ");
          String input1 = in.readLine();
          query += "'" + input1 + "'";
    
          System.out.print("Enter Flight Destination City: ");
          String input2 = in.readLine();
          query += " AND f.ArrivalCity = '" + input2 + "'";
    
          System.out.print("Enter Flight Date (YYYY-MM-DD): ");
          String input3 = in.readLine();
          query += " AND fi.FlightDate = '" + input3 + "' AND s.DayOfWeek = TRIM(TO_CHAR(fi.FlightDate, 'Day'))";
    
          query += " GROUP BY s.DepartureTime, s.ArrivalTime, fi.NumOfStops";
 
          // indexes created for ones in where and group by clause
          
          long start = System.currentTimeMillis();
    
          int rowCount = esql.executeQueryAndPrintResult(query);
    
          long end = System.currentTimeMillis();
    
          System.out.println("Execution Time (ms): " + (end - start));
    
          System.out.println ("Total Row(s): " + rowCount);
       }catch(Exception e){
          System.err.println (e.getMessage());
       }
    }
    
    public static void feature13(AirlineManagement esql, String authorisedUser) {
       // first check if the user is a Customer person, if not kick them back to the main menu
       String[] credentials = authorisedUser.split(",");
       String role = credentials[2];
  
       if (!role.equals("Customer")) {
          System.out.println("Sorry you can't complete this action as you are not a Customer.");
          return;
       }
  
       // read flight number to get ticket cost 
       System.out.println("\nTicket Cost");
       System.out.println("-----------");
       try {
          String query = "SELECT fi.TicketCost AS Ticket_Cost, fi.FlightDate AS Flight_Date, TRIM(TO_CHAR(fi.FlightDate, 'Day')) AS Day " +
                         "FROM FlightInstance fi " +
                         "WHERE fi.FlightNumber = ";
 
          // no need for new index
    
          System.out.print("Enter Flight Number: ");
          String input = in.readLine();
          query += "'" + input + "'";
    
          long start = System.currentTimeMillis();
    
          int rowCount = esql.executeQueryAndPrintResult(query);
    
          long end = System.currentTimeMillis();
    
          System.out.println("Execution Time (ms): " + (end - start));
    
          System.out.println ("Total Ticket(s): " + rowCount);
       }catch(Exception e){
          System.err.println (e.getMessage());
       }
    }
    
    public static void feature14(AirlineManagement esql, String authorisedUser) {
       // first check if the user is a Customer person, if not kick them back to the main menu
       String[] credentials = authorisedUser.split(",");
       String role = credentials[2];
  
       if (!role.equals("Customer")) {
          System.out.println("Sorry you can't complete this action as you are not a Customer.");
          return;
       }
  
       // read flight number to get the plaen make and model 
       System.out.println("\nAirplane Make and Model");
       System.out.println("-----------------------");
       try {
          String query = "SELECT p.Make, p.Model " +
                         "FROM Plane p Natural JOIN Flight f " +
                         "WHERE f.FlightNumber = ";
 
          // no need for new index
    
          System.out.print("Enter Flight Number: ");
          String input = in.readLine();
          query += "'" + input + "'";
    
          long start = System.currentTimeMillis();
    
          int rowCount = esql.executeQueryAndPrintResult(query);
          
          long end = System.currentTimeMillis();
    
          System.out.println("Execution Time (ms): " + (end - start));
    
          System.out.println ("Total Row(s): " + rowCount);
       }catch(Exception e){
          System.err.println (e.getMessage());
       }
    }
    
    public static void feature15(AirlineManagement esql, String authorisedUser) {
       // first check if the user is a Customer person, if not kick them back to the main menu
       String[] credentials = authorisedUser.split(",");
       String role = credentials[2];
       String username = credentials[0];
  
       if (!role.equals("Customer")) {
          System.out.println("Sorry you can't complete this action as you are not a Customer.");
          return;
       }
  
       
       System.out.println("\nMake a Reservation or Join Waitlist");
       System.out.println("-----------------------------------");
       try {
          // this to make a new reservation id so we can add to the table 
          String query1 = "SELECT MAX(r.ReservationID) FROM Reservation r";
 
          long start1 = System.currentTimeMillis();
 
          String mostRecentReservationID = esql.getValueQuery(query1);
 
          long end1 = System.currentTimeMillis();
 
          String numPart = mostRecentReservationID.substring(1);
          int mostRecentNum = Integer.parseInt(numPart);
          mostRecentNum += 1;
 
          String newResID = String.format("R%04d", mostRecentNum);
 
          // we need to get the customer id which is given when we add them as a user 
          // this is so when reservation is made we know which customer made it 
          String query2 = "SELECT c.CustomerID " +
                          "FROM Customer c, CustomerCredentials cs " +
                          "WHERE cs.username = '" + username + "' AND cs.custID = c.CustomerID";
          
          // created an index here for customer id, username is primary key does not need one
          // rest do no need them already created or is a primary key
          
          long start2 = System.currentTimeMillis();
          
          List<List<String>> customer_id = esql.executeQueryAndReturnResult(query2);
 
          long end2 = System.currentTimeMillis();
          
          // decided in our own interpretation, customer would have access to flight number so read it and flight date 
          System.out.print("Enter Flight Number: ");
          String fin = in.readLine();
 
          System.out.print("Enter Flight Date (YYYY-MM-DD): ");
          String fd = in.readLine();
 
          String query3 = "SELECT fi.FlightInstanceID " +
                          "FROM FlightInstance fi " +
                          "WHERE fi.FlightNumber = '";
 
          query3 += fin + "' AND fi.FlightDate = '" + fd + "'";
 
          long start3 = System.currentTimeMillis();
 
          String fiidstr = esql.getValueQuery(query3);
 
          long end3 = System.currentTimeMillis();
 
          int fiid = Integer.parseInt(fiidstr);
 
          // get the amount of seats left to see if we can reserve or waitlitst 
    
          String query4 = "SELECT (fi.SeatsTotal - fi.SeatsSold) AS SeatsLeft " +
                          "FROM FlightInstance fi " +
                          "WHERE fi.FlightInstanceID = ";
    
          query4 += fiid;
 
          long start4 = System.currentTimeMillis();
       
          List<List<String>> seat_status = esql.executeQueryAndReturnResult(query4);
 
          long end4 = System.currentTimeMillis();
    
          if(seat_status.isEmpty()) {
             System.out.println("Flight instance does not exist");
             return;
          } 
    
          int seats_left = Integer.parseInt(seat_status.get(0).get(0));
    
          String status = (seats_left > 0) ? "reserved" : "waitlist";
    
          long start5 = 0;
          long end5 = 0;
 
          // if a seat is reserved we add to the seats sold in flight instance
          if(status.equals("reserved")) {
             String query5 = "UPDATE FlightInstance " +
                             "SET SeatsSold = SeatsSold + 1 " +
                             "WHERE FlightInstanceID = ";
             
             query5 += fiid;
    
             start5 = System.currentTimeMillis();
    
             esql.executeUpdate(query5);
    
             end5 = System.currentTimeMillis();
          }
 
          // insert to it 
          String query6 = "INSERT INTO Reservation(ReservationID, CustomerID, FlightInstanceID, STATUS) " +
                          "VALUES (";
    
          query6 += "'" + newResID + "', '" + customer_id.get(0).get(0) + "', '" + fiid +  "', '" + status + "')";
    
          long start6 = System.currentTimeMillis();
    
          esql.executeUpdate(query6);
                   
          long end6 = System.currentTimeMillis();
 
          if (status.equals("reserved")) {
             System.out.println("Reserved Flight " + fin + " on " + fd);
          } else {
             System.out.println("Waitlisted Flight " + fin + " on " + fd);
          }
 
          long time1 = end1 - start1;
          long time2 = end2 - start2;
          long time3 = end3 - start3;
          long time4 = end4 - start4;
          long time5 = end5 - start5;
          long time6 = end6 - start6;
          
    
          System.out.println("Execution Time (ms): " + (time1 + time2 + time3 + time4 + time5 + time6));
       }catch(Exception e){
          System.err.println (e.getMessage());
       }
    }
 
    // Pilot Queries
    public static void makeRequest(AirlineManagement esql, String authorisedUser) {
       // first check if the user is a Pilot, if not kick them back to the main menu
       String[] credentials = authorisedUser.split(",");
       String role = credentials[2];
 
       if (!role.equals("Pilot")) {
          System.out.println("Sorry you can't complete this action as you are not a Pilot.");
          return;
       }
 
       // read PlaneID, RepairCode, RequestDate, PilotID from user
       System.out.println("\nMake Request");
       System.out.println("------------");
       try{
          String query1 = "SELECT MAX(R.RequestID) FROM MaintenanceRequest R";
 
          long start = System.currentTimeMillis();
          int mostRecentRequest = esql.executeCountQuery(query1);
 
          mostRecentRequest += 1;
          
          System.out.print("Enter Plane ID: ");
          String planeId = in.readLine();
 
          System.out.print("Enter Repair Code: ");
          String repairCode = in.readLine();
 
          System.out.print("Enter Request Date: ");
          String date = in.readLine();
 
          System.out.print("Enter Pilot ID: ");
          String pilotId = in.readLine();
 
          String query2 = "INSERT INTO MaintenanceRequest (RequestID, PlaneID, RepairCode, RequestDate, PilotID) VALUES (";
          query2 += mostRecentRequest + ", '" + planeId + "', '" + repairCode + "', '" + date + "', '" + pilotId + "')";
 
          System.out.print("Logging Request...");
 
          esql.executeUpdate(query2);
 
          long end = System.currentTimeMillis();
    
          System.out.println("Execution Time for all function queries (ms): " + (end - start));
 
          System.out.println("Done");
          System.out.println("Save Request ID for records: " + mostRecentRequest);
       }catch(Exception e){
          System.err.println (e.getMessage());
       }
    }
 
    // Technician Queries
    public static void viewPlaneRepairs(AirlineManagement esql, String authorisedUser) {
       // first check if the user is a Technician, if not kick them back to the main menu
       String[] credentials = authorisedUser.split(",");
       String role = credentials[2];
 
       if (!role.equals("Technician")) {
          System.out.println("Sorry you can't complete this action as you are not a Technician.");
          return;
       }
 
       // read planeID, startDate, and endDate from user
       System.out.println("\nRepairs Performed");
       System.out.println("-----------------");
       try{
          System.out.print("Enter Plane ID: ");
          String planeId = in.readLine();
          System.out.print("Enter Start Date (YYYY-MM-DD): ");
          String startDate = in.readLine();
          System.out.print("Enter End Date (YYYY-MM-DD): ");
          String endDate = in.readLine();
 
          String query = "SELECT R.PlaneID, R.RepairCode, R.RepairDate FROM Repair R WHERE R.PlaneID = '";
          query += planeId + "' AND R.RepairDate >= '" + startDate + "' AND R.RepairDate <= '" + endDate + "'";
          query += " ORDER BY R.RepairDate ASC";
 
          long start = System.currentTimeMillis();
          int numRepairs = esql.executeQueryAndPrintResult(query);
 
          long end = System.currentTimeMillis();
          System.out.println("Execution Time (ms): " + (end - start));
 
          System.out.println ("total repair(s): " + numRepairs);
       }catch(Exception e){
          System.err.println (e.getMessage());
       }
    }
 
    public static void viewPilotRequests(AirlineManagement esql, String authorisedUser) {
       // first check if the user is a Technician, if not kick them back to the main menu
       String[] credentials = authorisedUser.split(",");
       String role = credentials[2];
 
       if (!role.equals("Technician")) {
          System.out.println("Sorry you can't complete this action as you are not a Technician.");
          return;
       }
 
       // read PilotID from user
       System.out.println("\nPilot's Requests");
       System.out.println("----------------");
       try{
          System.out.print("Enter Pilot ID: ");
          String pilotId = in.readLine();
 
          String query = "SELECT R.PilotID, R.PlaneID, R.RepairCode, R.RequestDate FROM MaintenanceRequest R WHERE R.PilotID = '";
          query += pilotId + "' ORDER BY R.RequestDate ASC";
 
          long start = System.currentTimeMillis();
          int numRequests = esql.executeQueryAndPrintResult(query);
 
          long end = System.currentTimeMillis();
    
          System.out.println("Execution Time (ms): " + (end - start));
 
          System.out.println ("total request(s): " + numRequests);
       }catch(Exception e){
          System.err.println (e.getMessage());
       }
    }
 
    public static void completeRepair(AirlineManagement esql, String authorisedUser) {
       // first check if the user is a Technician, if not kick them back to the main menu
       String[] credentials = authorisedUser.split(",");
       String role = credentials[2];
 
       if (!role.equals("Technician")) {
          System.out.println("Sorry you can't complete this action as you are not a Technician.");
          return;
       }
 
       System.out.println("\nComplete Repair");
       System.out.println("---------------");
       try{         
          // read PlaneID, RepairCode, RepairDate, TechnicianID from user
          System.out.print("Enter Plane ID: ");
          String planeId = in.readLine();
 
          System.out.print("Enter Repair Code: ");
          String repairCode = in.readLine();
 
          System.out.print("Enter Date Completed (YYYY-MM-DD): ");
          String date = in.readLine();
 
          System.out.print("Enter Technician ID: ");
          String techId = in.readLine();
 
          System.out.print("Logging Repair...");
 
          // delete requests in MaintenanceRequest table pertaining to the specific plane and repair code
          String deleteQuery = "DELETE FROM MaintenanceRequest WHERE PlaneID = ";
          deleteQuery += "'" + planeId + "' " + "AND RepairCode = " + "'" + repairCode + "'";
 
          long start = System.currentTimeMillis();
          esql.executeUpdate(deleteQuery);
 
          // update LastRepairDate in Plane table pertaining to the specific plane
          String updateQuery = "UPDATE Plane SET LastRepairDate = '";
          updateQuery += date + "' WHERE PlaneID = " + "'" + planeId + "'";
 
          esql.executeUpdate(updateQuery);
 
          // insert new complete repair into Repair table
          String query1 = "SELECT MAX(R.RepairID) FROM Repair R";
          int mostRecentRepair = esql.executeCountQuery(query1);
          mostRecentRepair += 1;
 
          String query2 = "INSERT INTO Repair (RepairID, PlaneID, RepairCode, RepairDate, TechnicianID) VALUES (";
          query2 += mostRecentRepair + ", '" + planeId + "', '" + repairCode + "', '" + date + "', '" + techId + "')";
          
          esql.executeUpdate(query2);
 
          //String debugQuery = "SELECT * FROM Repair R WHERE R.RepairID = 16";
          //esql.executeQueryAndPrintResult(debugQuery);
 
          long end = System.currentTimeMillis();
    
          System.out.println("Execution Time for all function queries query (ms): " + (end - start));
 
          System.out.println("Done");
          System.out.println("Save Repair ID for records: " + mostRecentRepair);
       }catch(Exception e){
          //e.printStackTrace();
          System.err.println (e.getMessage());
       }
    }
   
 
 
 }//end AirlineManagement