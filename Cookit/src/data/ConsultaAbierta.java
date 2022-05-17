package data;

import java.sql.*;

public class ConsultaAbierta {

	// TODO: Add SQL inyection protection
	public Object[][] select(Connection con, String query, int columns) {

		PreparedStatement consulta;
		ResultSet resultado;
		Object[][] objeto = null;
		
		String queryCount = "";
		String queryCount2 = "";
		
		int rowCount = 0; 
		int j = 0;

		// Replace the select with a count(*)
		int selectStart = query.indexOf("SELECT") + 6;
		int selectEnd = query.lastIndexOf("FROM");
		queryCount = query.substring(0, selectStart) + " count(*) " + query.substring(selectEnd, query.length());
		queryCount2 = queryCount;

		// remove the order by
		if(queryCount.contains("ORDER BY")) {
			int orderStart = queryCount.indexOf("ORDER BY");
			int orderEnd = queryCount.indexOf("LIMIT");
			queryCount2 = queryCount.substring(0, orderStart) + " "
					+ queryCount.substring(orderEnd, queryCount.length());
		}

		try {

			// do a count(*) of que query received
//			System.out.println(queryCount2);
			consulta = con.prepareStatement(queryCount2);
			resultado = consulta.executeQuery();
			
			if (resultado.next()) {
				rowCount = resultado.getInt(1);
			}

			objeto = new Object[rowCount][columns];

//			System.out.println(query+"\n Rows: "+rowCount);
			consulta = con.prepareStatement(query);
			resultado = consulta.executeQuery();

			// fill the array with the resultSet
			while(resultado.next()) {
				for (int i = 0; i < columns; i++) {
//					System.out.println("j="+j+" i="+i+" cols="+columns);
					objeto[j][i] = resultado.getObject(i+1);
					//System.out.println("Pos."+i+" Objeto:"+objeto[j][i]);
				}
				//System.out.println("j número "+j);
				j ++;
			}

		} catch (SQLException sqle) {
			System.err.print("Hubo un error durante la ejecución de la consulta\n");
			sqle.printStackTrace();
		} catch (Exception ex) {
			System.err.print("Hubo un error desconocido durante la ejecución de la consulta\n");
			ex.printStackTrace();
		}

		return objeto;

	}

}
