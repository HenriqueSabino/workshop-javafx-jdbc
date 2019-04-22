package model.services;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;

import java.util.List;

public class DepartmentService {
    
    private DepartmentDao departmentDao = DaoFactory.createDepartmentDao();
    
    public List<Department> findAll() {
        return departmentDao.findAll();
    }
    
    public Department findByName(String name) {
        return departmentDao.findByName(name);
    }
    
    public void savaOrUpdate(Department department) {
        if (department.getId() == null) {
            departmentDao.insert(department);
        } else {
            departmentDao.update(department);
        }
    }
    
    public void remove(Department obj) {
        departmentDao.deleteById(obj.getId());
    }
}
