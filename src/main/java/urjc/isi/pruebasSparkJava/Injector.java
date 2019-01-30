package urjc.isi.pruebasSparkJava;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import java.util.Map;
import java.util.HashMap;


public class Injector {

	private static Connection c;

	public Injector(String name) {
		try {
//			if(sqlite){
//				c = DriverManager.getConnection("jdbc:sqlite:IMDb.db");
//				c = setAutoCommit(false);
//			}		    
		    String dbUrl = System.getenv(name);
		    c = DriverManager.getConnection(dbUrl);

			c.setAutoCommit(false);
		}catch (SQLException e) {
            throw new RuntimeException(e);
        }
	}

	public static Boolean filmExists(String title, String year) {
		String sql = "SELECT titleid FROM movies WHERE title = "+ title +"AND year = "+ year;
		try (PreparedStatement pstmt = c.prepareStatement(sql)) {
			ResultSet rs= pstmt.executeQuery();
			rs.next();
			rs.getInt("titleid");
			return true;
		}catch (SQLException e) {
			return false;
		}
	}


//	public static Boolean searchFilm(String title) {
//		String sql = "SELECT title FROM movies WHERE title = "+ title;
//		try (PreparedStatement pstmt = c.prepareStatement(sql)) {
//			ResultSet rs= pstmt.executeQuery();
//			rs.next();
//			rs.getString("title");
//			return true;
//		}catch (SQLException e) {
//			return false;
//		}
//	}

//	public static Boolean searchYear(String year) {
//		String sql = "SELECT year FROM movies WHERE year = "+ year;
//		try (PreparedStatement pstmt = c.prepareStatement(sql)) {
//			ResultSet rs= pstmt.executeQuery();
//			rs.next();
//			rs.getInt("year");
//			return true;
//		}catch (SQLException e) {
//			return false;
//		}
//	}
	
	public static Boolean searchTitleId(Integer titleID) {
		String sql = "SELECT titleid FROM movies WHERE titleid = "+ titleID;
		try (PreparedStatement pstmt = c.prepareStatement(sql)) {
			ResultSet rs= pstmt.executeQuery();
			rs.next();
			rs.getInt("titleid");
			return true;
		}catch (SQLException e) {
			return false;
		}
	}
	
	public static Boolean searchNameId(Integer NameID) {
		String sql = "SELECT nameid FROM workers WHERE nameid = "+ NameID;
		try (PreparedStatement pstmt = c.prepareStatement(sql)) {
			ResultSet rs= pstmt.executeQuery();
			rs.next();
			rs.getInt("nameid");
			return true;
		}catch (SQLException e) {
			return false;
		}
	}


	public static void insertFilm(String data1, String data2, String data3){
    	String sql="";
		int random = 0;
		//Comprobar elementos que son distintos que null
    	if(data1 == null || data2 == null){
    		throw new NullPointerException();
    	}
    	random = (int) (Math.random() * 1000)+1; //Ponemos más 1 para que no pueda haber titleid 0
    	while(searchTitleId(random)) {
    		random = (int) (Math.random() * 1000)+1;
    	}
    	sql = "INSERT INTO movies(titleid, title, year, genres) VALUES(?,?,?,?)";
    	try (PreparedStatement pstmt = c.prepareStatement(sql)) {       		
			pstmt.setInt(1, random);		
			pstmt.setString(2, data1);
        	pstmt.setInt(3, Integer.valueOf(data2));
        	pstmt.setString(4, data3);
        	pstmt.executeUpdate();
        	c.commit();
        } catch (SQLException e) {
        	System.out.println(e.getMessage());
        }

    }

	public static void insertActor(String data1){
    	String sql="";
		Integer random = 0;
		//Comprobar elementos que son distintos que null
    	if(data1 == null){
    		throw new NullPointerException();
    	}
    	random = (int) (Math.random() * 1000)+1; //Ponemos más 1 para que no pueda haber titleid 0
    	while(searchNameId(random)) {
    		random = (int) (Math.random() * 1000)+1;
    	}
    	sql = "INSERT INTO workers(nameid, primary_name) VALUES(?,?)";

    	try (PreparedStatement pstmt = c.prepareStatement(sql)) {  
    		pstmt.setInt(1, random);
			pstmt.setString(2, data1);
        	pstmt.executeUpdate();
        	c.commit();
    	} catch (SQLException e) {
    	   	 System.out.println(e.getMessage());
    	}
    }

	public static Integer selectTitle_ID(String title, String year) {
		String sql = "SELECT titleid FROM movies WHERE title = "+ title + " AND year = " + year;
		try (PreparedStatement pstmt = c.prepareStatement(sql)) {
			ResultSet rs= pstmt.executeQuery();
			rs.next();
			return rs.getInt("titleid");
		}catch (SQLException e) {
			return null;
		}
	}
	
	public static Integer selectName_ID(String name) {
		String sql = "SELECT nameid FROM workers WHERE primary_name = "+ name;
		try (PreparedStatement pstmt = c.prepareStatement(sql)) {
			ResultSet rs= pstmt.executeQuery();
			rs.next();
			return rs.getInt("nameid");
		}catch (SQLException e) {
			return null;
		}
	}
	
	public static void insertWorks_In(Integer data1, Integer data2){
    	String sql="";
		//Comprobar elementos que son distintos que null
    	if(data1 == null || data2 == null){
    		throw new NullPointerException();
    	}
    		sql = "INSERT INTO works_in(titleID, nameID) VALUES(?,?)";

    	try (PreparedStatement pstmt = c.prepareStatement(sql)) {
    		pstmt.setInt(1, data1);
    		pstmt.setInt(2, data2);
    		pstmt.executeUpdate();
    	} catch (SQLException e) {
    	    System.out.println(e.getMessage());
    	}
    }

	

	public List<String> filterByName(String film) {
		String sql = "SELECT * FROM movies WHERE title = "+"'"+film+"'";
		List<String> result = new ArrayList<String>();

    	try (PreparedStatement pstmt = c.prepareStatement(sql)) {
    		ResultSet rs = pstmt.executeQuery();
    		c.commit();
    		if (rs.next()) {
        		String id=Integer.toString(rs.getInt("titleid"));
                String title = rs.getString("title");
                String year = Integer.toString(rs.getInt("year"));
                String runtimeMinutes = Integer.toString(rs.getInt("runtime_minutes"));
                String averageRating = (Double.toString(rs.getDouble("average_rating"))).substring(0, 3);
                String numVotes = Integer.toString(rs.getInt("num_votes"));
                String genres = rs.getString("genres");
                result.add(title);
                result.add(year);
                result.add(runtimeMinutes);
                result.add(averageRating);
                result.add(numVotes);
                result.add(genres);
                result.add(id);
    		}
    	} catch (SQLException e) {
    		System.out.println(e.getMessage());
    	}
    	return result;
	}

	public List<String> filterByYear(String year) {
		String sql = "SELECT * FROM movies WHERE year = "+"'"+year+"'";
		List<String> result = new ArrayList<String>();

    	try (PreparedStatement pstmt = c.prepareStatement(sql)) {
    		ResultSet rs = pstmt.executeQuery();
    		c.commit();
    		while(rs.next()) {
                String title = rs.getString("title");
                result.add(title);
            }
    	} catch (SQLException e) {
    		System.out.println(e.getMessage());
    	}
    	return result;
	}

	public List<String> filterByDuration(String minutes) {
		String sql = "SELECT * FROM movies WHERE runtime_minutes <= "+"'"+minutes+"'";
		List<String> result = new ArrayList<String>();

    	try (PreparedStatement pstmt = c.prepareStatement(sql)) {
    		ResultSet rs = pstmt.executeQuery();
    		c.commit();
    		while(rs.next()) {
                String title = rs.getString("title");
                result.add(title);
            }
    	} catch (SQLException e) {
    		System.out.println(e.getMessage());
    	}
    	return result;
	}

	public List<String> filterByRating(String rating) {
		String sql = "SELECT * FROM movies WHERE average_rating >= "+"'"+rating+"'";
		List<String> result = new ArrayList<String>();

    	try (PreparedStatement pstmt = c.prepareStatement(sql)) {
    		ResultSet rs = pstmt.executeQuery();
    		c.commit();
    		while(rs.next()) {
                String title = rs.getString("title");
                result.add(title);
            }
    	} catch (SQLException e) {
    		System.out.println(e.getMessage());
    	}
    	return result;
	}

	public float meanScores(int film) {
		String sql = "SELECT avg(score) FROM ratings WHERE titleid ="+film;
    	float result = 0;

    	try (PreparedStatement pstmt = c.prepareStatement(sql)) {
    		
    		ResultSet rs = pstmt.executeQuery();
    		c.commit();
    		if (rs.next()) {
    			result = rs.getFloat("avg");
    		}
    	} catch (SQLException e) {
    		System.out.println(e.getMessage());
    	}
    	return result;
	}
	
	public String[][] userandcomments(String film){
		String sql = "SELECT comment,clientID FROM Comments JOIN movies ON movies.titleID = Comments.titleID JOIN clients ON clients.clientID=movies.clientID WHERE movies.title LIKE "+"+film+"+" GROUP BY clientID";
		
		String name_col= "clientID";
		String name_col2= "commentID";
		String table = "Comments";
		String table2 = "clients";
		Integer total_comment = 0;
		Integer total_clients = 0;
		total_comment = contar(table,name_col);
		total_clients = contar(table2,name_col2);
		String[][] result = new String[total_clients][total_comment];
		Integer aux = 0;
		
		try (PreparedStatement pstmt = c.prepareStatement(sql)) {
    		ResultSet rs = pstmt.executeQuery();
    		c.commit();

    		while(rs.next()) {
    			aux = rs.getInt("ClientID");
    			for (int i = 0; i< total_comment;i++) {
    				if (result[aux-1][i] == null) {
    					result[aux-1][i] = rs.getString("comment");
    					break;
    				}
    			}
   		}
		} catch (SQLException e) {
			
    		System.out.println(e.getMessage());
    	}
		return result;
	}

	public Integer contar(String name_table,String name_col) {
		String sql = "SELECT COUNT("+name_col+") FROM "+ name_table;
		Integer result = 0;

		try (PreparedStatement pstmt = c.prepareStatement(sql)) {
    		ResultSet rs = pstmt.executeQuery();
    		c.commit();
    		result = rs.getInt("COUNT("+name_col+")");
    	} catch (SQLException e) {

    		System.out.println(e.getMessage());
    	}
		return result;
	}

	public List<String> getFilmComments(int film){
		String sql = "SELECT clientid, comment FROM comments WHERE titleid="+film;
		
		List<String> result = new ArrayList<String>();
		
		try (PreparedStatement pstmt = c.prepareStatement(sql)) {
    		ResultSet rs = pstmt.executeQuery();
    		c.commit();

    		while(rs.next()) {
    			String title = Integer.toString(rs.getInt("clientid"))+" : "+rs.getString("comment");
                result.add(title);
   		}
		} catch (SQLException e) {

    		System.out.println(e.getMessage());
    	}
		return result;
	}

	public List<String> filterByGenre(String genre) {
		String sql = "SELECT title FROM movies WHERE genres LIKE '%"+genre+"%'";
		List<String> result = new ArrayList<String>();

    	try (PreparedStatement pstmt = c.prepareStatement(sql)) {
    		ResultSet rs = pstmt.executeQuery();
    		c.commit();
    		while(rs.next()) {

                String title = rs.getString("title");
                result.add(title);
            }
    	} catch (SQLException e) {
    		System.out.println(e.getMessage());
    	}
    	return result;
	}

	public List<String> filterByActorActress(String name) {
		String sql = "SELECT title FROM movies JOIN works_in ON movies.titleid=works_in.titleid ";
		sql+= "JOIN workers ON workers.nameid=works_in.nameid ";
		sql += "WHERE workers.primary_name LIKE "+ "'" + name +"'";
		sql += " and (works_as LIKE 'actor' or works_as LIKE 'actress')";
		sql += " ORDER BY movies.titleid DESC";
		List<String> result = new ArrayList<String>();

    	try (PreparedStatement pstmt = c.prepareStatement(sql)) {
    		ResultSet rs = pstmt.executeQuery();
    		c.commit();
    		while(rs.next()) {
                String title = rs.getString("title");
                result.add(title);
            }
    	} catch (SQLException e) {
    		System.out.println(e.getMessage());
    	}
    	return result;
	}

	public void makeDataHashMap(Map<Integer, Map<Integer, Double>> data) {
		String sql = "SELECT * FROM ratings ORDER BY clientid;";

		try (PreparedStatement pstmt = c.prepareStatement(sql)) {
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
			Integer titleid = rs.getInt("titleid");
			Integer clientid = rs.getInt("clientid");
			Double score = rs.getDouble("score");

			if (!data.containsKey(clientid)) {
				data.put(clientid, new HashMap<Integer, Double>());
			}

			data.get(clientid).put(titleid, score);
			}

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}


    public Boolean searchRating(Integer titleID, Integer clientID) {
		String sql = "SELECT score FROM ratings WHERE titleID = "+ titleID;
		sql += " and clientID = "+ clientID;
		try (PreparedStatement pstmt = c.prepareStatement(sql)) {
			ResultSet rs= pstmt.executeQuery();
			rs.next();
			rs.getInt("score");
			return true;
		}catch (SQLException e) {
			return false;
		}
	}
    
    public int searchScore(Integer titleID, Integer clientID) {
		String sql = "SELECT score FROM ratings WHERE titleID = "+ titleID;
		sql += " and clientID = "+ clientID;
		try (PreparedStatement pstmt = c.prepareStatement(sql)) {
			ResultSet rs= pstmt.executeQuery();
			rs.next();
			int score=rs.getInt("score");
			return score;
		}catch (SQLException e) {
			return 0;
		}
	}

    public void insertRating(Integer titleid, Integer clientid, Integer score) {
	String sql= new String();

    	if(searchRating(titleid, clientid)) {
    		sql = "UPDATE ratings SET score=" + score;
    		sql += " WHERE titleid=" + titleid + " and clientid="+ clientid;
    		try (PreparedStatement pstmt = c.prepareStatement(sql)) {
        		pstmt.executeUpdate();
        		c.commit();
        	} catch (SQLException e) {
        		System.out.println(e.getMessage());
        	}
    	}else {
    		sql = "INSERT INTO ratings(titleid, clientid,score) VALUES(?,?,?)";
    		try (PreparedStatement pstmt = c.prepareStatement(sql)) {
        		pstmt.setInt(1, titleid);
        		pstmt.setInt(2, clientid);
        		pstmt.setInt(3, score);
        		pstmt.executeUpdate();
        		c.commit();
        	} catch (SQLException e) {
        		System.out.println(e.getMessage());
        	}
    	}
    }

    public Boolean searchUser(Integer clientID) {
		String sql = "SELECT clientID FROM clients WHERE clientid = "+ clientID;
		try (PreparedStatement pstmt = c.prepareStatement(sql)) {
			ResultSet rs= pstmt.executeQuery();
			rs.next();
			rs.getInt("clientID");
			return true;
		}catch (SQLException e) {
			return false;
		}
	}

	public void insertUser(Integer clientid) {
		String sql= new String();

    	if(!searchUser(clientid) ){
    		sql = "INSERT INTO clients(clientID) VALUES("+clientid+")";
    		try (PreparedStatement pstmt = c.prepareStatement(sql)) {
        		pstmt.executeUpdate();
        		c.commit();
        	} catch (SQLException e) {
        		System.out.println(e.getMessage());
        	}
    	}
    }
	
	public int getNumComments() {
		String sql = "SELECT MAX(\"commentId\") FROM comments";
    	try (PreparedStatement pstmt = c.prepareStatement(sql)) {   
    		ResultSet rs = pstmt.executeQuery();
    		if(rs.next()){
    			int lastId = rs.getInt("max");
    			return(lastId);
    		}
    			
    	} catch (SQLException e) {
    		System.out.println(e.getMessage());
    	}
    	return(0);
	}
	
	public List<String> commentById(int id_comment) {
		String sql = "SELECT * FROM comments WHERE \"commentId\" =" + id_comment;
		List<String> result = new ArrayList<String>();

    	try (PreparedStatement pstmt = c.prepareStatement(sql)) {
    		ResultSet rs = pstmt.executeQuery();
    		c.commit();
    		if (rs.next()) {
                String titleid = Integer.toString(rs.getInt("titleid"));
                String clientid = Integer.toString(rs.getInt("clientid"));
                String comment =rs.getString("comment");
                result.add(titleid);
                result.add(clientid);
                result.add(comment);
    		}
    	} catch (SQLException e) {
    		System.out.println(e.getMessage());
    	}
    	return result;
	} 
	
	public void deleteComment(String id_comment) {
		String sql = "DELETE FROM comments WHERE \"commentId\" =" + id_comment;
		try (PreparedStatement pstmt = c.prepareStatement(sql)) {
    		pstmt.executeUpdate();
    		System.out.println(id_comment);
    		c.commit();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}


//titleid, clientID y comment
//NOMBRE TABLA: comments(Hay que crearla)
    public void insertComments(Integer titleid, Integer clientid, String comment) {
    	String sql = "SELECT MAX(\"commentId\") FROM comments";
    	try (PreparedStatement pstmt = c.prepareStatement(sql)) {   
    		ResultSet rs = pstmt.executeQuery();
    		if(rs.next()){
    			int lastId = rs.getInt("max");
    			System.out.println(lastId);
    	
    			sql= "INSERT INTO comments(\"commentId\",titleid, clientid, comment) VALUES(?,?,?,?)";
    			try (PreparedStatement pstmt2 = c.prepareStatement(sql)) {
		   			pstmt2.setInt(1, lastId+1);			
		   			pstmt2.setInt(2, titleid);
		   	    	pstmt2.setInt(3, clientid);
		   	    	pstmt2.setString(4, comment);
		   	    	pstmt2.executeUpdate();
		   	    	c.commit();
    			} catch (SQLException e) {
            		System.out.println(e.getMessage());
            	}	
		   	    
    		}
    	} catch (SQLException e) {
        		System.out.println(e.getMessage());
        }
   	}

    public void updateAverageRating(Integer titleID, Float averageRating) {
		String sql = "UPDATE movies SET average_rating = " + averageRating; 
		sql += " WHERE titleid = " + titleID;
		try (PreparedStatement pstmt = c.prepareStatement(sql)) {
    		pstmt.executeUpdate();
    		c.commit();
    	} catch (SQLException e) {    		
    		System.out.println(e.getMessage());
    	}
		
	}

	public void close() {
        try {
            c.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
