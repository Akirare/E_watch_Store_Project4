package filter;

import Entity.Employees;
import java.io.IOException;
import java.io.PrintWriter;
import static java.lang.System.out;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class Logger implements Filter
{

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("=======================================filter.Logger.init()");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException 
    {
        System.out.println("filter.Logger.doFilter()");
        HttpServletRequest  httpReqeust             =   (HttpServletRequest) request;
        HttpServletResponse httpServletResponse     =   (HttpServletResponse) response;
        HttpSession         session                 =   httpReqeust.getSession();
        
        Employees emp = (Employees) session.getAttribute("CurrentEmployee");
                
        if( emp != null )
        {
            chain.doFilter(request, response);
        }
        else
        {
            response.setContentType("text/html");
            PrintWriter pw=response.getWriter();
            pw.println("<script type=\"text/javascript\">");
            pw.println("alert('You must Login');");
            pw.println("</script>");
            RequestDispatcher rd=request.getRequestDispatcher("faces/admin_Login.xhtml");
            rd.include(request, response);
        }
    }

    @Override
    public void destroy() {
        System.out.println("=======================================filter.Logger.destroy()");
    }

    
    
}
