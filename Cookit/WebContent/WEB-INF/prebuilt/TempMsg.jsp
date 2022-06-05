<%

boolean showMsg = false;
String tempMsg = null;
String success = "false";

if(request.getAttribute("success") != null){
	success = (String) request.getAttribute("success");
}

if(request.getAttribute("showMsg") != null){
	showMsg = (boolean) request.getAttribute("showMsg");
}

if(request.getAttribute("tempMsg") != null) {
	tempMsg = (String) request.getAttribute("tempMsg");
	showMsg = true;
}

%>

<%-- The tempMessage --%>
	<% if(showMsg == true){
		if(!tempMsg.isEmpty()){
			switch(success){
			case "true":
				out.print("<div class='tempMsg successMsg'><p>"+tempMsg+"</p></div>");
				break;
			case "false":
				out.print("<div class='tempMsg errorMsg'><p>"+tempMsg+"</p></div>");
				break;
			default:
				out.print("<div class='tempMsg infoMsg'><p>"+tempMsg+"</p></div>");
				break;
			}
		}
	} %>