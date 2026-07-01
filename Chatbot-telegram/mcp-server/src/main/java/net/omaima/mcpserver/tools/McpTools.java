package net.omaima.mcpserver.tools;

import org.springaicommunity.mcp.annotation.McpArg;
import org.springaicommunity.mcp.annotation.McpTool;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class McpTools {
    @McpTool(name="getEmployee", description="Get information about a given employee ()")
    public Employee getEmployee(@McpArg(description="The employee's name") String name){
        return new Employee(name, 123000, 4);
    }

    @McpTool(description="Get All Employees")
    public List<Employee> getAllEmployees(){
        return List.of(
                new Employee("Hassan", 12300, 4),
                new Employee("Omaima", 11800, 3),
                new Employee("Imane", 23000, 10)
        );
    }
}

record Employee(String name, double salary, int seniority){

}
