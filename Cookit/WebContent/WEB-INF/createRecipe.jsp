<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="ISO-8859-1"%>
    
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Cookit - Crear receta</title>

    <link rel="stylesheet" href="css/main.css">
    <link rel="stylesheet" href="css/recipeMod.css">
    
    <jsp:include page="prebuilt/boostrap.jsp" />
    
</head>
<body>

<%@ page import="data.BeanReceta"%>
<%@ page import="data.BeanCategoria"%>

	<%
	
		//Object[][] category = (Object[][]) session.getAttribute("categories");
	BeanCategoria[] category = (BeanCategoria[]) session.getAttribute("categories");	
	
		BeanReceta savedRecipe = new BeanReceta();
		boolean load = false;
		
		if(session.getAttribute("savedRecipe") != null){
			savedRecipe = (BeanReceta) session.getAttribute("savedRecipe");
			load = true;
		}
	
	%>

	<div id="content">

        <jsp:include page="prebuilt/header.jsp" />
        <jsp:include page="prebuilt/tempMsg.jsp" />

        <div class="recipe">
            <form action="uploadRecipe" method="POST" enctype="multipart/form-data">

                <div class="recipe-name">
                    <div class="recipe-title">
                        <label for="title">*T&iacute;tulo</label>
                        <input type="text" name="title" id="title" <% if(load){ out.print("value='"+savedRecipe.getTitulo()+"'"); } %> required>
                    </div>
                    <div class="recipe-subtitle" title="(Opcional) Una breve descripci%oacute;n sobre la receta o el resultado final">
                        <label for="title">Subt&iacute;tulo</label>
                        <input type="text" name="subtitle" id="subtitle" <% if(load){ out.print("value='"+savedRecipe.getSubtitulo()+"'"); } %>>
                    </div>
                </div>

                <div class="recipe-info">

                    <div class="recipe-img">
                  			<img src='loadImg?id=<%= savedRecipe.getIdReceta() %>&target=recipe' id='preview' onerror="this.onerror=null;this.src='img/broken.jpg';this.title='Oops... Ha ocurrido un problema. Elija otra imagen'"/>
                  			<button onclick='event.preventDefault(); document.getElementById("getFile").click()'>A&ntilde;adir una imagen A</button>
                  			<input type='file' name='img' value="" id='getFile' style='display: none;'>
                      
                    </div>

                    <div class="requirements">
                    
                    <div class="category">
                        	<label for="category" title="Elija la categor&iacute;a correcta para que su receta sea encontrada m&aacute;s facilmente&#13;Utilizar una categor&iacute;a err&oacute;nea puede resultar en el bloqueo de su publicaci&oacute;n por parte de los administradores&#13;&#13;Ante la duda utilice la categor&iacute;a general">Categor&iacute;a</label>
                        	<select name="category" required>
                        		<% for(int i = 0; i < category.length; i++){  %>
                        			<option value="<%= (int) category[i].getId() %>" title="<%= (String) category[i].getDescripcion() %>" <% if(load){if(i == savedRecipe.getId_categoria()){ out.print("selected"); }} %> ><%= (String) category[i].getNombre() %></option>
                        		<% } %>
                        	</select>
                        </div>
                        
                        
                        <div class="tags"
                            title="Escriba las palabras claves separadas por coma y con buena ortograf&iacute;a&#13;Esto ayudar&aacute; a que su receta sea encontrada m&aacute;s facilmente&#13;&#13;Ejemplo:pizza, tomate, queso, champi&ntilde;ones, horneado">
                            <label>Palabras clave (tags)</label>
                            <textarea rows="3" cols="30" name="tags"><% if(load){ %><%=savedRecipe.getTags() %><% } %></textarea>
                        </div>
                        <div class="cooking-time">
                            <label>*Preparaci&oacute;n en minutos</label>
                            <input type="number" name="time" <% if(load){ out.print("value='"+savedRecipe.getTiempo()+"'"); } else { out.print("value='10'"); }%> min="1" required/>
                        </div>
                        <div class="ingredients"
                            title="Escriba los ingredientes separados por coma y con buena ortograf&iacute;a&#13;si lo desea tambien indique la cantidad&#13;Esto ayudar&aacute; a que su receta sea encontrada y preparada m&aacute;s facilmente&#13;&#13;Ejemplo:hojaldre, agua 100ml, medio tomate, un pimiento verde, 300g de queso parmesano">
                            <label>*Ingredientes</label>
                            <textarea rows="3" cols="30" name="ingredients" required><% if(load){ out.print(savedRecipe.getIngredientes()); } %></textarea>
                        </div>

                    </div>
                </div>

                <div class="procedure"
                    title="Para mantener las recetas f&aacute;ciles de seguir, resumelas en 750 caracteres">
                    <label>*Procedimiento (<span id="length-display">0</span>/750)</label>
                    <textarea rows="10" cols="75" id="length-target" name="procedure" required><% if(load){ out.print(savedRecipe.getProcedimiento());} %></textarea>
                </div>

                <div class="form-options">
                    <input type="submit" name="submitRecipe" value="Publicar" />
                    <input type="submit" name="submitRecipe" value="Guardar" />
                </div>

            </form>

            <div class="close">
                <a href="profile"><button>Salir</button></a>
            </div>

        </div>

    </div>
    
    <jsp:include page="prebuilt/footer.jsp" />
    
    <script src="js/previewImg.js"></script>
    <script src="js/length.js"></script>

</body>
</html>