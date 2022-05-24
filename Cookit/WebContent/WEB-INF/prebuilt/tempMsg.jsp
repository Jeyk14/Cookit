<%

	String tempMsg = null;
	boolean success = false;

	if(session.getAttribute("succes") != null){
		success = (boolean) session.getAttribute("success");
	}
	
	if(session.getAttribute("tempMsg") != null) {
		tempMsg = (String) session.getAttribute("tempMessage");
	}
%>

<%if(tempMsg != null){ %>
	
		<% if(session.getAttribute("success") == "true"){ %>
		
			<div class="tempMsg successMsg">
				<h4>Muy bien</h4>
				<p><%= request.getAttribute("tempMsg") %></p>
			</div>
		
		<% } else { %>
		
			<div class="tempMsg errorMsg">
				<h4>Parece que algo ha ido mal...</h4>
				<p><%= request.getAttribute("tempMsg") %></p>
			</div>
		
		<% }  session.removeAttribute("tempMsg"); session.removeAttribute("success"); }%>