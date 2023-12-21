//Alan Mispagel

import java.io.*;
import java.nio.file.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.*;
import static java.util.Calendar.*;

public class AccountUtility  {

    private ArrayList<CheckingAccount> accounts = null;
    private File accountsFile = null;

    private final String FIELD_SEP = "<>";

    public AccountUtility() throws ParseException
    {
        accountsFile = new File("accounts.txt");
        accounts = this.getCheckingAccounts();
    }

    public ArrayList<CheckingAccount> getCheckingAccounts()throws ParseException
    {   //copies file into an array list **MUST have exactly 4 parts**
        
        // if the accounts file has already been read, don't read it again
        if (accounts != null)
            return accounts;        

        accounts = new ArrayList<>();        
        
        if (accountsFile.isFile())  // prevent the FileNotFoundException
        {
            try (BufferedReader in = 
                     new BufferedReader(
                     new FileReader(accountsFile)))
            {
                // read all accounts stored in the file
                // into the array list
                String line = in.readLine();
                while(line != null)
                {
                    String[] columns = line.split(FIELD_SEP);
                    String number = columns[0];
                    String name = columns[1];
                        String[] date = columns[2].split("/");
                        GregorianCalendar openDate = new GregorianCalendar(
                                        Integer.parseInt(date[0]), 
                                        Integer.parseInt(date[1]) - 1, 
                                        Integer.parseInt(date[2]));
                    String balance = columns[3];
                        
                    CheckingAccount ca = new CheckingAccount(
                        number, name, openDate, Double.parseDouble(balance));

                    accounts.add(ca);

                    line = in.readLine(); //read the next line                   
                }
                in.close();
            }
            catch(IOException e)
            {
                System.out.println(e);
                return null;
            }
        }
        return accounts;            
    }

    public CheckingAccount getCheckingAccount(String code)
    { //access a particular account in the array list
        for (CheckingAccount p : accounts)
        {
            if (p.number.equals(code))
                return p;
        }
        return null;
    }
    public boolean Accountexists(String toac)
    {
        boolean is = false;
        for (CheckingAccount p : accounts)
            {
                if (p.number.equals(toac))
                    is = true;
            }
        return is;
    }

    public void updateCheckingAccount(CheckingAccount newCheckingAccount)
    { // get the old product and remove it, as well as add the parameter 
        CheckingAccount oldCheckingAccount = 
                        this.getCheckingAccount(newCheckingAccount.number);
        int i = accounts.indexOf(oldCheckingAccount);
        accounts.remove(i);

        // add the updated account
        accounts.add(i, newCheckingAccount);
    }

    public boolean saveCheckingAccounts() 
    {    //outputs the arraylist to txt file
        
        try (PrintWriter out = new PrintWriter(
                           new BufferedWriter(
                           new FileWriter(accountsFile))))
        {
        for (CheckingAccount p : accounts){
            out.print(p.number + FIELD_SEP);
            out.print(p.name + FIELD_SEP);
            out.print(p.openDate.get(YEAR) + "/" +
                     (p.openDate.get(MONTH)+1) + "/" +
                      p.openDate.get(DAY_OF_MONTH) + FIELD_SEP);
            out.print(String.valueOf(p.balance));
            out.print("\n");}
            
        return true;
        }
        catch(IOException e)
        {
            System.out.println(e);
            return false;
        }
    }	
} 
