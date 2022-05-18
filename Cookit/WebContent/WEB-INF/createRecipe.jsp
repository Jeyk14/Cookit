<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Cookit - Crear receta</title>

    <link rel="stylesheet" href="css/main.css">
    <link rel="stylesheet" href="css/recipe.css">
</head>
<body>

<%@ page import="data.BeanReceta"%>

	<%
	
		Object[][] category = (Object[][]) session.getAttribute("categories");
		
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
                    <div class="recipe-subtitle" title="(Opcional) Una breve descripci�n sobre la receta o el resultado final">
                        <label for="title">Subt&iacute;tulo</label>
                        <input type="text" name="subtitle" id="subtitle" <% if(load){ out.print("value='"+savedRecipe.getSubtitulo()+"'"); } %>>
                    </div>
                </div>

                <div class="recipe-info">

                    <div class="recipe-img">
                    	<% if(load){ out.print("<img src='loadImg?id="+savedRecipe.getIdReceta()+"&target=recipe' id='preview' />"); } else { out.print("<img src='img/placeholder.png' id='preview' />"); } %>
                        <button onclick="event.preventDefault(); document.getElementById('getImg').click()">A&ntilde;adir una imagen</button>
                        <input type="file" name="img" id="getImg" style="display: none;">
                    </div>

                    <div class="requirements">
                    
                    <div class="category">
                        	<label for="category" title="Elija la categor�a correcta para que su receta sea encontrada m�s facilmente&#13;Utilizar una categor�a err�nea puede resultar en el bloqueo de su publicaci�n por parte de los administradores&#13;&#13;Ante la duda utilice la categor�a general">Categor�a</label>
                        	<select name="category" required>
                        		<% for(int i = 0; i < category.length; i++){  %>
                        			<option value="<%= (int) category[i][0] %>" title="<%= (String) category[i][2] %>" <% if(load){if(i == savedRecipe.getId_categoria()){ out.print("selected"); }} %> ><%= (String) category[i][1] %></option>
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
                    title="Para mantener las recetas f&aacute;ciles de seguir, resumelas en 1.000 caracteres">
                    <label>*Procedimiento</label>
                    <textarea rows="10" cols="75" name="procedure" required><% if(load){ out.print(savedRecipe.getProcedimiento());} %></textarea>
                </div>

                <div class="form-options">
                    <input type="submit" name="submitRecipe" value="Publicar" />
                    <input type="submit" name="submitRecipe" value="Guardar" />
                </div>

            </form>

            <div class="close">
                <a href="WEB/INF/profie.jsp"><button>Salir</button></a>
            </div>

        </div>

    </div>
    
    <jsp:include page="prebuilt/footer.jsp" />

</body>
</html>