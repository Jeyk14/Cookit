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

	<%
	
		Object[][] category = (Object[][]) session.getAttribute("categories");
	
	%>

	<div id="content">

        <jsp:include page="prebuilt/header.jsp" />

        <div class="recipe">
            <form action="uploadRecipe" method="POST">

                <div class="recipe-name">
                    <div class="recipe-title">
                        <label for="title">*T&iacute;tulo</label>
                        <input type="text" name="title" id="title" required>
                    </div>
                    <div class="recipe-subtitle" title="(Opcional) Una breve descripciï¿½n sobre la receta o el resultado final">
                        <label for="title">Subt&iacute;tulo</label>
                        <input type="text" name="subtitle" id="subtitle">
                    </div>
                </div>

                <div class="recipe-info">

                    <div class="recipe-img">
                        <img src="img/placeholder.png" />
                        <button onclick="event.preventDefault(); document.getElementById('getImg').click()">A&ntilde;adir una imagen</button>
                        <input type="file" name="img" id="getImg" style="display: none;" required>
                    </div>

                    <div class="requirements">
                        <div class="tags"
                            title="Escriba las palabras claves separadas por coma&#13;Ejemplo:pizza, tomate, queso, champi&ntilde;ones, horneado">
                            <label>Palabras clave (tags)</label>
                            <textarea rows="3" cols="30" name="tags"></textarea>
                        </div>
                        <div class="cooking-time">
                            <label>*Tiempo de preparaci&oacute;n</label>
                            <input type="number" name="time" value="1" min="1" required/>
                        </div>
                        <div class="ingredients"
                            title="Escriba los ingredientes separados por coma,&#13;si lo desea tambien indique la cantidad&#13;Ejemplo:hojaldre, agua 100ml, medio tomate, pimiento, 300g de queso parmesano">
                            <label>*Ingredientes</label>
                            <textarea rows="3" cols="30" name="ingredients" required></textarea>
                        </div>
                        <div class="category">
                        	<label for="category">Categoría</label>
                        	<select name="category" required>
                        		<% for(int i = 0; i < category.length; i++){  %>
                        			<option value="<%= (int) category[i][0] %>" title="<%= (String) category[i][2] %>"><%= (String) category[i][1] %></option>
                        		<% } %>
                        	</select>
                        </div>
                    </div>
                </div>

                <div class="procedure"
                    title="Para mantener las recetas f&aacute;ciles de seguir, resumelas en 1.000 caracteres">
                    <label>*Procedimiento</label>
                    <textarea rows="10" cols="75" name="procedure" required></textarea>
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