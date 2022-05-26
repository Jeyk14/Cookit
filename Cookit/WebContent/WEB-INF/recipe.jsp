<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="ISO-8859-1"%>
<%@ page import="data.BeanUsuario"%>
<%@ page import="data.BeanComentario"%>
<%@ page import="data.BeanReceta"%>
<%@ page import="data.BeanCategoria"%>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Cookit - Receta</title>

    <link rel="stylesheet" href="css/main.css">
    <link rel="stylesheet" href="css/recipe.css">

</head>

<body>

	<%
	
	BeanUsuario myself = null;
	BeanReceta recipe = new BeanReceta();
	BeanComentario[] comments = null;
	BeanUsuario[] users = null;
	BeanCategoria[] categories = null;
	int contCom = 0;
	int auxId = 0;
	
	char liked = 'x';
	if(request.getAttribute("liked") != null){
		liked = (char) request.getAttribute("liked");
	}
	
	if(session.getAttribute("myself") != null){
		myself = (BeanUsuario) session.getAttribute("myself");
	}
		
	if(request.getAttribute("viewedPost") != null){
		recipe = (BeanReceta) request.getAttribute("viewedPost");
		auxId = recipe.getId_categoria();
	}
	
	if(request.getAttribute("comments") != null){
		comments = (BeanComentario[]) request.getAttribute("comments");
	}
	
	if(request.getAttribute("users") != null){
		users = (BeanUsuario[]) request.getAttribute("users");
	}
	
	if(session.getAttribute("categories") != null){
		categories = (BeanCategoria[]) session.getAttribute("categories");
	}
	
	%>

    <jsp:include page="prebuilt/header.jsp" />
    <jsp:include page="prebuilt/tempMsg.jsp"/>

    <div id="content" style="margin-top: 6em;">

        <div class="recipe">

            <div class="recipe-content">
                
                <%-- 
                <div class="recipe-subtitle">
                	<h5><%= recipe.getSubtitulo() %></h5>
                </div>--%>

                <div class="recipe-info">

                    <div style="position: relative;" class="recipe-image">
	                    <div style="position:absolute; top: -3em; left: -3%;" class="recipe-title title">
		                    <h3><%= recipe.getTitulo() %></h3>
	                	</div>
                        <img src='loadImg?id=<%= recipe.getIdReceta() %>&target=recipe' onerror="this.onerror=null;this.src='img/broken.jpg';this.title='Oops... Ha ocurrido un problema cargando la imagen'"/>
                    	<div class="recipe-subtitle" style="text-align: center; font-style: italic;">
                			<p>"<%= recipe.getSubtitulo() %>"</p>
                		</div>
                    </div>

                    <div class="recipe-data">
                        <div class="category" title="<%= categories[auxId].getDescripcion() %>">
                            <p><%= categories[recipe.getId_categoria()].getNombre() %></p>
                        </div>

                        <div class="tags">
                            <p>
                                Palabras clave (tags)
                            </p>
                            <textarea><%= recipe.getTags() %></textarea>
                        </div>

                        <div class="ingredients">
                            <p>
                                Ingredientes
                            </p>
                            <textarea><%= recipe.getIngredientes() %></textarea>
                        </div>

                    </div>

                </div>

                <div class="procedure">
                    <div>
                        <p>Procedimiento de preparación en X minutos</p>
                    </div>
                    <textarea><%= recipe.getProcedimiento() %></textarea>
                </div>

            </div>
            
            <% if(myself != null){ %>

            <div class="like-dislike">
                <div>
                    <a <% if(liked == 'L'){ out.print("class='liked'"); } else { out.print("href='like?type=1&user="+myself.getId()+"&post="+recipe.getIdPublicacion()+"'");}%>><button>
                            <img class="smallimg" src="img/star.png" />Me gusta
                        </button></a>
                </div>
                
                <div>
                    <a <% if(liked == 'D'){ out.print("class='disliked'"); } else { out.print("href='like?type=0&user="+myself.getId()+"&post="+recipe.getIdPublicacion()+"'");}%>><button>
                            <img class="smallimg" src="img/star_0.png" />No me gusta
                        </button></a>
                </div>
            </div>
            
            <% } %>

        </div>

        <div class="separator"></div>

        <div class="add-comment">
            <form>
                <div class="new-comment">
                    <div class="write-comment">
                        <h4>Añadir comentario:</h4>
                    </div>

                    <div class="comment-body">
                        <textarea name="comment"></textarea>
                    </div>
                    <div class="comment-options">
                        <input type="submit" value="Guardar comentario">
                    </div>
                </div>
            </form>
        </div>

        <div class="comment-rows" id="comments">
        
        <% while(comments[contCom] != null && contCom > 20){ %>
        
        	<div class="comment">
                <div class="comment-header">
                    <div class="comment-author">
                        <p><%= users[contCom].getNombre() %></p>
                    </div>
                    <div class="comment-date">
                        <p><%= comments[contCom].getFecha() %></p>
                    </div>
                </div>

                <div class="comment-body">
                    <textarea><%= comments[contCom].getTexto() %></textarea>
                </div>
                <div class="comment-edit">
                    <div class="delete?id_com=<%= comments[contCom].getId() %>&id_user=<%= users[contCom].getId() %>"><a href=""><button>Borrar</button></a></div>
                    <div class="edit" id="edit_<%= contCom %>"><button>Editar</button></div>
                </div>
            </div>
        
        <% } %>

        </div>

    </div>

   <jsp:include page="prebuilt/footer.jsp"/>

</body>

</html>