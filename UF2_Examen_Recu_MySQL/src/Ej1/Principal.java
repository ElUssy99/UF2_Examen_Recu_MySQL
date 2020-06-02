package Ej1;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.Scanner;

import javax.swing.plaf.synth.SynthSpinnerUI;

import org.omg.Messaging.SyncScopeHelper;

public class Principal {
	
	
	
	public static Connection connect() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3308/ventas?useUnicode=true&amp;useJDBCCompliantTimezoneShift=true&amp;useLegacyDatetimeCode=false&amp;serverTimezone=UTC", "root", "");
			System.out.println("Conexion establecida correctametne");
			return conn;
		} catch (Exception e) {
			System.out.println(e);
		}
		return null;
	}
	
	public static void main(String[] args) {
//		insertar(connect());
		menu();
	}
	
	public static void menu() {
		Scanner entrada = new Scanner(System.in);
		boolean continuar = true;
		
		while (continuar) {
			System.out.println("// INSERTAR DATOS VENTAS //");
			System.out.println("1. Insertar Ventas");
			System.out.println("2. Mostrar Ventas de un Cliente");
			System.out.println("3. Salir");
			System.out.println("Escoge una opcion:");
			int opcion = entrada.nextInt();
			
			switch (opcion) {
			case 1:
				insertarVentas(connect());
				break;
			case 2:
				mostrarDatos(connect());
				break;
			case 3:
				continuar = false;
				break;
			default:
				break;
			}
		}
		
		
	}
	
	public static void insertar(Connection conn) {
		try {
			String p1 = "INSERT INTO productos VALUES (1, 'producto 1', 5, 2, 100);";
			String p2 = "INSERT INTO productos VALUES (2, 'producto 2', 6, 3, 101);";
			String c1 = "INSERT INTO clientes VALUES (1, 'David', 'direccion 1', 'poblacion 1', 615089414, '15879X');";
			String c2 = "INSERT INTO clientes VALUES (2, 'Juan', 'direccion 2', 'poblacion 2', 684235485, '65879G');";
			String v1 = "INSERT INTO ventas VALUES (1, '2020-7-04', 1, 2, 5);";
			String v2 = "INSERT INTO ventas VALUES (2, '2019-8-25', 2, 1, 6);";
			
			Statement st = conn.createStatement();
			st.execute(p1);
			st.execute(p2);
			st.execute(c1);
			st.execute(c2);
			st.execute(v1);
			st.execute(v2);
			
		} catch (Exception e) {
			System.err.println(e);
		}
	}
	
	public static void insertarVentas(Connection conn) {
		Scanner entrada = new Scanner(System.in);
		
		boolean idventa = false;
		boolean idprod = true;
		boolean idcli = true;
		
		// Pido los datos al usuario
		System.out.println("Inserta los siguientes datos");
		System.out.print("IDVenta: ");
		int idv = entrada.nextInt();
		System.out.print("IDCliente: ");
		int idc = entrada.nextInt();
		System.out.print("IDProducto: ");
		int idp = entrada.nextInt();
		System.out.print("Cantidad: ");
		int cantidad = entrada.nextInt();
		
		// Comprueba tabla por tabla, si el ID que pone el Usuario existe o no.
		// Utiliza booleans para hacer despues la comprobacion.
		try {
			String qIdVenta = "SELECT * FROM ventas;";
			Statement stIdVenta = conn.createStatement();
			stIdVenta.execute(qIdVenta);
			
			ResultSet rsIdVentas = stIdVenta.getResultSet();
			while (rsIdVentas.next()) {
				if (rsIdVentas.getInt(1) == idv) {
					idventa = true;
				}
			}
			
			String qIdProd = "SELECT * FROM productos;";
			Statement stIdProd = conn.createStatement();
			stIdProd.execute(qIdProd);
			
			ResultSet rsIdProd = stIdProd.getResultSet();
			while (rsIdProd.next()) {
				if (rsIdProd.getInt(1) != idp) {
					idcli = false;
				}
			}
			
			String qIdCli = "SELECT * FROM clientes;";
			Statement stIdCli = conn.createStatement();
			stIdCli.execute(qIdCli);
			
			ResultSet rsIdCli = stIdProd.getResultSet();
			while (rsIdCli.next()) {
				if (rsIdCli.getInt(1) != idp) {
					idventa = false;
				}
			}
		}catch (Exception e) {
			System.err.println(e);
		}
		
		if (idventa == false && idprod == true && idcli == true) {
			if (cantidad > 0) {
				try {
					// No hace falta poner la Fecha porque en la Tabla, se pone la del dia actual automaticamente.
					
					String query = "INSERT INTO ventas (IDVENTA, IDCLIENTE, IDPRODUCTO, CANTIDAD) VALUES (?,?,?,?);";
					
					PreparedStatement ps = conn.prepareStatement(query);
					
					ps.setInt(1, idv);
					ps.setInt(2, idc);
					ps.setInt(3, idp);
					ps.setInt(4, cantidad);
					
					ps.execute();
					
					System.out.println("Se ha insertado la Venta.");
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
			} else {
				System.err.println("ERROR: La cantidad debe ser mayor a 0.");
			}
		} else {
			System.err.println("ERROR: Uno de los ID no estan bien.");
		}
	}

	public static void mostrarDatos(Connection conn) {
		Scanner entrada = new Scanner(System.in);
		
		System.out.println("Introduce el ID den Cliente");
		System.out.print("ID: ");
		int id = entrada.nextInt();
		
		try {
			String query = "SELECT * FROM ventas WHERE IDCLIENTE (SELECT IDCLIENTE FROM clientes WHERE IDCLIENTE = " + id + ");";
			Statement st = conn.createStatement();
			st.execute(query);
			
			ResultSet rs = st.getResultSet();
			while (rs.next()) {
				
			}
		} catch (Exception e) {
			System.err.println(e);
		}
	}
	
}
