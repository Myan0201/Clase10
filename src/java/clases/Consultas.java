package clases;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

public class Consultas {

    private String baseDatos;
    private String usuario;
    private String contra;
    private Connection miConexion;

    public Consultas(String BD, String US, String PS) {
        baseDatos = BD;
        usuario = US;
        contra = PS;
    }

    public Connection conectar() {

        Connection miConexion = null;

        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();//Obtenmos el driver de mysql
            miConexion = DriverManager.getConnection("jdbc:mysql://localhost/" + baseDatos, usuario, contra);//Conectamos a nuestra data

        } catch (SQLException e) {
            System.out.println(e.toString());
            miConexion = null;
        } catch (Exception e) {
            System.out.println(e.toString());
            miConexion = null;
        }
        return miConexion;
    }

    public String[] getFila(String consulta) {
        Vector vector = consulta(consulta);
        String[] fila = null;

        if (vector != null) // todo OK!
        {
            if (vector.size() > 1) // hay filas
            {
                fila = (String[]) vector.get(1);	// en 0 están los títulos
            }
        }
        return fila;
    }

    public Vector consulta(String consulta) {
        Vector regs = new Vector();

        try {
            Connection cn = conectar();

            if (cn == null) {
                regs = null;
            } else {
                Statement st = cn.createStatement();
                ResultSet rs = st.executeQuery(consulta);
                ResultSetMetaData rm = rs.getMetaData();
                int numCols = rm.getColumnCount();

                // Toma los títulos de las columnas
                String[] titCols = new String[numCols];
                for (int i = 0; i < numCols; ++i)//recorre las filas de la Tabla
                {
                    titCols[i] = rm.getColumnName(i + 1);
                }

                // la fila 0 del vector lleva los títulos de las columnas
                regs.add(titCols);

                // toma las filas de la consulta
                while (rs.next()) {
                    String[] reg = new String[numCols];

                    for (int i = 0; i < numCols; i++) {
                        reg[i] = rs.getString(i + 1);
                    }
                    regs.add(reg);
                }
                rs.close();
                st.close();
                cn.close();
            }
        } catch (SQLException e) {
            regs = null;
        } catch (Exception e) {
            regs = null;
        }
        return regs;
    }

    public int cantidadfilas(String consulta) {
        int numRows = 0;

        try {
            Connection miConexion = conectar();
            PreparedStatement miStatement = miConexion.prepareStatement(consulta);
            ResultSet myrs = miStatement.executeQuery();
            ResultSetMetaData rm = myrs.getMetaData();
            while (myrs.next()) {
                numRows++;
            }
        } catch (SQLException ex) {
            System.out.println("no conecta " + ex.getMessage());
        }//fin catch
        return numRows;
    }

    public int cantidadColumnas(String consulta) {
        int numCols = 0;

        try {
            Connection miConexion = conectar();

            PreparedStatement miStatement = miConexion.prepareStatement(consulta);
            ResultSet myrs = miStatement.executeQuery();
            ResultSetMetaData rm = myrs.getMetaData();

            numCols = rm.getColumnCount();

        } catch (SQLException ex) {
            System.out.println("no conecta " + ex.getMessage());
        }//fin catch
        return numCols;
    }

    public String[][] cargarUsuarios(String consulta) {
        String[][] datos = null;

        try {
            Connection miConexion = conectar();
            PreparedStatement miStatement = miConexion.prepareStatement(consulta);
            ResultSet myrs = miStatement.executeQuery();
            ResultSetMetaData rm = myrs.getMetaData();

            int columnas = rm.getColumnCount();
            int filas = cantidadfilas(consulta);

            datos = new String[filas][columnas];

            int i = 0;
            while (myrs.next()) {

                for (int j = 0; j < columnas; j++) {
                    int indice = j + 1;
                    datos[i][j] = myrs.getString(indice);
                }
                i++;
            }
            miConexion.close();
            
        } catch (SQLException ex) {
            System.out.println("no conecta " + ex.getMessage());
        }//fin catch
        return datos;
    }

    public boolean agregarDato(String consulta) {
        boolean validar = false;

        try {
            Connection miConexion = conectar();

            PreparedStatement ps = miConexion.prepareStatement(consulta);
            ps.execute();
            ps.close();

            miConexion.close();

            validar = true;
        } catch (SQLException ex) {
            validar = false;
        }//fin catch
        return validar;
    }

    public boolean modificarUsuario(int id, String pass, String nombre, String tipo) {
        boolean validar = false;

        try {

            Connection miConexion;
            miConexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + baseDatos + "?autoReconnect=true&useSSL=false", usuario, contra);

            PreparedStatement miStatement = miConexion.prepareStatement("UPDATE usuarios SET pass = ?, nombre = ?, tipo=? where id=?");
            miStatement.setString(1, pass);
            miStatement.setString(2, nombre);
            miStatement.setString(3, tipo);
            miStatement.setInt(4, id);
            miStatement.executeUpdate();
            miStatement.close();
            validar = true;

            miConexion.close();
        } catch (SQLException ex) {
            validar = false;
        }//fin catch
        return validar;
    }

    public boolean modificarCita(int Numeroregistro, String idenPaciente, String idenMedico, String fechacita) {
        boolean validar = false;

        try {
            Connection miConexion;
            miConexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + baseDatos + "?autoReconnect=true&useSSL=false", usuario, contra);

            PreparedStatement miStatement = miConexion.prepareStatement("UPDATE citas SET IdPaciente = ?, IdMedico = ?, FechaCita=? where NumRegistro=?");

            miStatement.setString(1, idenPaciente);
            miStatement.setString(2, idenMedico);
            miStatement.setString(3, fechacita);
            miStatement.setInt(4, Numeroregistro);
            miStatement.executeUpdate();
            miStatement.close();
            validar = true;

            miConexion.close();
        } catch (SQLException ex) {

            validar = false;

        }//fin catch

        return validar;
    }

    public boolean modificarExpediente(int idUsuario, String nombreUsuario, String domicilioUsuario, String telefonoUsuario, String padecimientoUsuario) {
        boolean validar = false;

        try {

            Connection miConexion;
            miConexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + baseDatos + "?autoReconnect=true&useSSL=false", usuario, contra);

            PreparedStatement miStatement = miConexion.prepareStatement("UPDATE expedientes SET nombre=?, domicilio=?, telefono=?, padecimiento=? where id=?");
            miStatement.setString(1, nombreUsuario);
            miStatement.setString(2, domicilioUsuario);
            miStatement.setString(3, telefonoUsuario);
            miStatement.setString(4, padecimientoUsuario);
            miStatement.setInt(5, idUsuario);
            miStatement.executeUpdate();
            miStatement.close();
            validar = true;

            miConexion.close();
        } catch (SQLException ex) {
            validar = false;
        }//fin catch
        return validar;
    }

    public boolean modiExpeMedico(int idUsuario, String nombreUsuario, String padecimientoUsuario) {
        boolean validar = false;

        try {

            Connection miConexion;
            miConexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + baseDatos + "?autoReconnect=true&useSSL=false", usuario, contra);

            PreparedStatement miStatement = miConexion.prepareStatement("UPDATE expedientes SET nombre=?, padecimiento=? where id=?");
            miStatement.setString(1, nombreUsuario);
            miStatement.setString(2, padecimientoUsuario);
            miStatement.setInt(3, idUsuario);
            miStatement.executeUpdate();
            miStatement.close();
            validar = true;

            miConexion.close();
        } catch (SQLException ex) {
            validar = false;
        }//fin catch
        return validar;
    }

    public boolean eliminarUsuario(int id) {

        boolean validar = false;
        try {
            Connection miConexion;
            miConexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + baseDatos + "?autoReconnect=true&useSSL=false", usuario, contra);

            PreparedStatement miStatement = miConexion.prepareStatement("DELETE FROM usuarios where id=?");
            miStatement.setInt(1, id);
            miStatement.executeUpdate();
            miStatement.close();
            validar = true;

        } catch (SQLException ex) {
            validar = false;
        }//fin catch
        return validar;
    }

    public boolean eliminarExpediente(int ID) {

        boolean validar = false;
        try {
            Connection miConexion;
            miConexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + baseDatos + "?autoReconnect=true&useSSL=false", usuario, contra);

            PreparedStatement miStatement = miConexion.prepareStatement("DELETE FROM expedientes where id=?");
            miStatement.setInt(1, ID);
            miStatement.executeUpdate();
            miStatement.close();
            validar = true;

        } catch (SQLException ex) {
            validar = false;
        }//fin catch
        return validar;
    }

    public boolean eliminarCita(int NumRegistro) {

        boolean validar = false;
        try {
            Connection miConexion;
            miConexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + baseDatos + "?autoReconnect=true&useSSL=false", usuario, contra);

            PreparedStatement miStatement = miConexion.prepareStatement("DELETE FROM citas where NumRegistro=?");
            miStatement.setInt(1, NumRegistro);
            miStatement.executeUpdate();
            miStatement.close();

            validar = true;

        } catch (SQLException ex) {
            validar = false;
        }//fin catch
        return validar;
    }
}
