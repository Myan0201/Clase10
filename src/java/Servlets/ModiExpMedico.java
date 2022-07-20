
package Servlets;

import clases.Consultas;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ModiExpMedico extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        try (PrintWriter out = response.getWriter()) {
            int idUsuario = Integer.parseInt(request.getParameter("ID"));
            String nombreUsuario = request.getParameter("Nombre");
            String padecimientoUsuario = request.getParameter("Padecimiento");

            Consultas cons = new Consultas("clinica", "root", "");

            boolean validado = cons.modiExpeMedico(idUsuario, nombreUsuario, padecimientoUsuario);

            if (validado) {
                response.sendRedirect("ExpedientesMedico.jsp");
                
            } else {
                out.println("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<head>");
                out.println("<title>Modificar expedientes</title>");

                /*Hojas de estilo*/
                out.println("<link rel=\'stylesheet\' href=\'https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/css/bootstrap.min.css\' integrity=\'sha384-TX8t27EcRE3e/ihU7zmQxVncDAy5uIKz4rEkgIXeMed4M0jlfIDPvg6uqKI2xXr2\' crossorigin=\'anonymous\'>");
                out.println("<link rel=\'stylesheet\' href=\'https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/css/bootstrap.min.css\' integrity=\'sha384-TX8t27EcRE3e/ihU7zmQxVncDAy5uIKz4rEkgIXeMed4M0jlfIDPvg6uqKI2xXr2\' crossorigin=\'anonymous\'>");
                out.println("<script src=\'https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/js/bootstrap.bundle.min.js\' integrity=\'sha384-ho+j7jyWK8fNQe+A12Hb8AhRq26LrZ/JpcUGGOn+Y7RsweNrtN/tE3MoK7ZeZDyx\' crossorigin=\'anonymous\'></script>");
                out.print("<link href=\'CSS/hoja.css\' rel=\'stylesheet\'>");

                out.println("</head>");
                out.println("<body>");

                out.println("<div class='Encabezado'><h2>Doctor <%=nombre%></h2></div>");
                out.println("<div class='Contenido'>");
                out.println("<br><h1>No se pudo modificar la cita: " + idUsuario + "</h1><br>");
                out.println("<br><a href='ExpedientesMedico.jsp'><button class='btn btn-secondary'>Volver</button></a>");
                out.println("</div>");
                out.println("<div class='Pie'><h6>2020 - Yancy Elizondo</h6></div>");
                out.println("</body>");
                out.println("</html>");
            }
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
