package urjc.isi.pruebasSparkJava.score_comments;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.*;

import urjc.isi.pruebasSparkJava.Comment;
import urjc.isi.pruebasSparkJava.Injector;



public class CommentTest {
	
	
	Injector I = new Injector("JDBC_DATABASE_URL");

	
	//tests for newComment(String text, int user, String film) 
	
	// test for null comment
	@Test
	public void testforNullText(){
		Comment comment = new Comment();
		assertEquals ("Comentario invalido", comment.newComment(null, 5, "Kill Bill: Volumen 3", I));
		I.close();
	}
	
	//test for invalid user
	@Test 
	public void testforInvalidUser(){
		Comment comment = new Comment();
		assertEquals("Usuario invalido", comment.newComment("comentario", -1, "Kill Bill: Volumen 3", I));
		I.close();
	}
	
	//test for invalid film
	@Test
	public void testforNullFilm(){
		Comment comment = new Comment();
		assertEquals ("Pelicula invalida", comment.newComment("comentario", 4, null, I));
		I.close();
	}
	
	
	//test for commentsFilm(String film)
	
	//test for invalid film
	@Test 
	public void testforInvalidFilm(){
		Comment comment = new Comment();
		assertEquals ("Pelicula invalida", comment.commentsFilm(-1, I));
		I.close();
	}
	
	//test for commentToString (String matrix_coment[][])
	
	@Test 
	public void testforNullComments(){
		Comment comment = new Comment();

		assertEquals("No tiene comentarios", comment.commentToString(null));
		I.close();
	}
	
	//happy path tests
	
	@Test
	public void newCommentTest(){
		Comment comment = new Comment();
		int id_comment=I.getNumComments()+1;
		assertEquals ("Comentario almacenado", comment.newComment("comentario", 4, "Titanic", I));
		System.out.println(id_comment);
		List<String> comment_info=I.commentById(id_comment);
		System.out.println(id_comment);
		assertEquals ("comentario", comment_info.get(2));
		assertEquals (4, Integer.parseInt(comment_info.get(1)));
		assertEquals (120338, Integer.parseInt(comment_info.get(0)));
		System.out.println(id_comment);
		I.deleteComment(Integer.toString(id_comment));
		I.close();
	}
		
	@Test
	public void commentsFilmTest(){
		Comment comment = new Comment();
		String resultc;
		resultc = comment.commentsFilm(332452, I);
		assertNotNull("Comments cant be null", resultc);
		I.close();
		
	}
	
	@Test
	public void commentToStringTest(){
		Comment comment = new Comment();
		String resultc;
		String [][] comments = {{"comentario 1", "comentario 2"}};
		resultc = comment.commentToString(comments);
		assertNotNull("Comments cant be null", resultc);
		I.close();
	}
	/*	
	@Test
	public void postCommentTest(){
		Request req;
		Comment comment = new Comment();
		String resultc;
		resultc = comment.postComment(req, I);
		assertNotNull("comments cant be null", resultc);
	}*/

}
