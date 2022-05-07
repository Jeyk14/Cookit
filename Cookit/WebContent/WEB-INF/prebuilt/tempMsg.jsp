<%if(request.getAttribute("tempMsg") != null){%>
	
	<% if(request.getAttribute("success") != null){ %>
	
		<% if(request.getAttribute("success") == "true"){ %>
		
			<div class="tempMsg successMsg">
				<h4>Parece que algo ha ido mal...</h4>
				<p><%= request.getAttribute("tempMsg") %></p>
			</div>
		
		<% } else { %>
		
			<div class="tempMsg errorMsg">
				<h4>Parece que algo ha ido mal...</h4>
				<p><%= request.getAttribute("tempMsg") %></p>
			</div>
		
		<% } %>
	
	<% } else { %>
	
		<div class="tempMsg">
			<h4>Parece que algo ha ido mal...</h4>
			<p><%= request.getAttribute("tempMsg") %></p>
		</div>
		
	<% } %>
	
<%}%>