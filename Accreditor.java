import java.sql.*;
import java.util.*; // ArrayList, Random
import javax.swing.JOptionPane;


public class Accreditor{

	String myMatric,myFName,mySName,myDept,mySchool, myYear,mySex,myEmail,myPin;

	public Accreditor(String matric){
		myMatric = matric;
	}
	// calls all the other private and inherited methods
	final String DATABASE_URL = "jdbc:mysql://localhost/vote";	

	Connection connection;
	Statement statement;
	ResultSet resultSet;

	private void connect() throws SQLException{

		try{
			connection = DriverManager.getConnection(DATABASE_URL, "root", "mysqlrootpassword");
		}catch(SQLException sqlErr){
			sqlErr.printStackTrace();
			JOptionPane.showMessageDialog(null, "Could not connect to Database\nPlease try again" + sqlErr.getMessage(), "ERROR OCCURED", JOptionPane.ERROR_MESSAGE);
		}
	}

	protected void closeConnection() throws SQLException{
		try{
			connection.close();
			statement.close();
			resultSet.close();
		}catch(Exception err){
			err.printStackTrace();
		}
	}

	protected void getStringDetails() throws SQLException{
		connect();
		String query_string_details = "SELECT fname, sname, department, school, year, sex FROM students WHERE matric = " + this.myMatric;// image is missing
		try{
			statement = connection.createStatement();
			resultSet = statement.executeQuery(query_string_details);
			while(resultSet.next()){
				this.myFName = resultSet.getString("fname");
				this.mySName = resultSet.getString("sname");
				this.myDept = resultSet.getString("department");
				this.mySchool = resultSet.getString("school");
				this.myYear = resultSet.getString("year");
				this.mySex = resultSet.getString("sex");
			}
			//this.myEmail = resultSet.getString("email");
			//Image myImage = resultSet.get
			//String myDetails = "Here are your details: " + myName + "\n" + myMatric + "\n" + myDept + "\n" + mySchool + "\n" + myYear + "\n" + mySex;
		}catch(SQLException error){
			error.printStackTrace();
		}
	}

	private boolean confirmPresence()throws SQLException{
		connect();
		boolean isAStudent = false;
		String cfmQuery = "SELECT 1 FROM students WHERE matric = ?";
		try {
        	PreparedStatement prepStatement = connection.prepareStatement(cfmQuery);
        	if (prepStatement != null) {
           		 prepStatement.setString(1, this.myMatric);

            	try{
            		ResultSet rSet = prepStatement.executeQuery();
            		if(rSet != null){
            			try{
            				if(rSet.next()){
            					isAStudent = true;
            				}
            			}catch(Exception rSetException){
            				rSetException.printStackTrace();
            			}
            			rSet.close();
            		} 
            	}catch(Exception statementException){
            		statementException.printStackTrace();
            	}
            	//statement.close();
       		}
       	}catch(Exception genException){
       		genException.printStackTrace();
       	}
       	connection.close();
       	return isAStudent;
	}

	public void accredit() throws SQLException{
		connect();
		try{
			if(confirmPresence()){
				if(!isAccredited()){
					generatePin();
					addPinToDB();
					if(quicklyVerifyPin()){
						JOptionPane.showMessageDialog(null, "You have been successfully accredited\nYour pin is " + this.myPin + "Please keep it secure, you will need it to vote.\nHowever, it will be sent to the email you provided","Success", JOptionPane.INFORMATION_MESSAGE);
						activateAccreditation();
					}
					else{
						JOptionPane.showMessageDialog(null, "An error occured, please try again","Error", JOptionPane.ERROR_MESSAGE);
					}
				}
				else{
					// you have been accredited
					JOptionPane.showMessageDialog(null, "This student has been accredited", "You have been accredited", JOptionPane.INFORMATION_MESSAGE);
				}
			}
			else{
				JOptionPane.showMessageDialog(null, "You are not a registered student\nYour name was not found in the school database");	
			}
		}catch(SQLException bad){
			bad.printStackTrace();
		}
	}
	// makes a new pin, saves it in this.pin
	private void generatePin(){
		GeneratePin generatePin = new GeneratePin();
		this.myPin = generatePin.getPin();
	}

	//checks if the generated pin has been saved
	private boolean quicklyVerifyPin() throws SQLException{
		boolean allFine = false;
		String verifyQuery = "SELECT * FROM students WHERE matric = '" + this.myMatric +"'";
		connect();
		try{
			statement = connection.createStatement();
			resultSet = statement.executeQuery(verifyQuery);
			while(resultSet.next()){
				String thePin = resultSet.getString("pin");
				if(thePin.equals(this.myPin)){
					allFine = true;
				}else{
					allFine = false;
				}
			}
		}catch(SQLException baddy){
				baddy.printStackTrace();
		}finally{
			closeConnection();
		}
		return allFine;
	}
	private void activateAccreditation()throws SQLException{
		String query = "UPDATE students SET accredited = ? WHERE matric = ?";
		connect();
		PreparedStatement setAccrStat = null;
		try{
			setAccrStat = connection.prepareStatement(query);
			setAccrStat.setInt(1, 1);
			setAccrStat.setString(2, this.myMatric);
			setAccrStat.executeUpdate();
		}catch(SQLException anErr){
			anErr.printStackTrace();
			JOptionPane.showMessageDialog(null, "Sorry, something happened We are trying to fix it" + anErr.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
		}finally{
			closeConnection();
			setAccrStat.close();
		}
	}
	private boolean isAccredited()throws SQLException{
		String check = "SELECT accredited FROM students WHERE matric = '" + this.myMatric +"'";
		connect();
		boolean valid = false;
		Statement state = connection.createStatement();
		ResultSet resSet;
		try{
			resSet = state.executeQuery(check);
			int a = resSet.getInt("accredited");
			if(a == 1 || a != 0){
				valid = true;
			}
		}catch(SQLException e){
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
		}
		return valid;
	}

	// adds the pin to the database
	private void addPinToDB() throws SQLException{
		String addQuery = "UPDATE students SET pin = ? WHERE matric = ?" ;
		connect();	
		PreparedStatement pStatement = null;
		try{
			pStatement = connection.prepareStatement(addQuery);
			pStatement.setString(1, this.myPin);
			pStatement.setString(2, this.myMatric);
			pStatement.executeUpdate();
		}catch(SQLException err404){
			err404.printStackTrace();
		}finally{
			closeConnection();
			pStatement.close();
		}
	}	
}