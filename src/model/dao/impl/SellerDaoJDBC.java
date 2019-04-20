package model.dao.impl;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SellerDaoJDBC implements SellerDao {

    Connection connection;

    public SellerDaoJDBC(Connection connection){
        this.connection = connection;
    }

    @Override
    public void insert(Seller obj) {
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(
                    "INSERT INTO seller "
                    + "(Name, Email, BirthDate, BaseSalary, DepartmentId)"
                    + "VALUES (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS
            );

            preparedStatement.setString(1, obj.getName());
            preparedStatement.setString(2, obj.getEmail());
            preparedStatement.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
            preparedStatement.setDouble(4, obj.getBaseSalary());
            preparedStatement.setInt(5, obj.getDepartment().getId());

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0){
                ResultSet resultSet = preparedStatement.getGeneratedKeys();
                if (resultSet.next()){
                    int id = resultSet.getInt(1);
                    obj.setId(id);
                }
                DB.closeResultSet(resultSet);
            }

            else{
                throw new DbException("Unexpected error! No rows affected");
            }
        }
        catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeStatement(preparedStatement);
        }
    }

    @Override
    public void update(Seller obj) {
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(
              "UPDATE seller SET " +
                      "Name = ?, Email = ?, BirthDate = ?, " +
                      "BaseSalary = ?, DepartmentId = ? " +
                      "WHERE seller.Id = ?"
            );

            preparedStatement.setString(1, obj.getName());
            preparedStatement.setString(2,obj.getEmail());
            preparedStatement.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
            preparedStatement.setDouble(4, obj.getBaseSalary());
            preparedStatement.setInt(5, obj.getDepartment().getId());
            preparedStatement.setInt(6, obj.getId());

            preparedStatement.executeUpdate();
        }
        catch (SQLException e){
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeStatement(preparedStatement);
        }
    }

    @Override
    public void deleteById(Integer id) {
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(
                    "DELETE FROM seller " +
                            "WHERE Id = ?");

            preparedStatement.setInt(1, id);

            preparedStatement.executeUpdate();
        }
        catch (SQLException e){
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeStatement(preparedStatement);
        }
    }

    @Override
    public Seller findById(Integer id) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = connection.prepareStatement("SELECT "
            + "seller.*, department.Name AS DepName FROM "
            + "seller INNER JOIN department ON "
            + "seller.DepartmentId = department.Id "
            + "WHERE seller.Id = ?");

            preparedStatement.setInt(1, id);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                Department department = instantiateDepartment(resultSet);

                Seller seller = instantiateSeller(resultSet, department);

                return seller;
            }
            else
                return null;
        }
        catch (SQLException e){
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeStatement(preparedStatement);
            DB.closeResultSet(resultSet);
        }
    }

    private Department instantiateDepartment(ResultSet resultSet) throws SQLException {
        Department department = new Department();

        department.setId(resultSet.getInt("DepartmentId"));
        department.setName(resultSet.getString("DepName"));

        return department;
    }

    private Seller instantiateSeller(ResultSet resultSet, Department department) throws SQLException {
        Seller seller = new Seller();
        seller.setDepartment(department);

        seller.setId(resultSet.getInt("Id"));
        seller.setName(resultSet.getString("Name"));
        seller.setEmail(resultSet.getString("Email"));
        seller.setBirthDate(resultSet.getDate("BirthDate"));
        seller.setBaseSalary(resultSet.getDouble("BaseSalary"));

        return seller;
    }

    @Override
    public List<Seller> findAll() {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = connection.prepareStatement(
                    "SELECT seller.*, department.Name AS DepName "
                    + "FROM seller INNER JOIN department "
                    + "ON seller.DepartmentId = department.Id "
                    + "ORDER BY Name"
            );

            resultSet = preparedStatement.executeQuery();
            List<Seller> sellers = new ArrayList<>();
            Map<Integer, Department> deps = new HashMap<>();

            while (resultSet.next()){
                Department dep = deps.get(resultSet.getInt("DepartmentId"));

                if (dep == null){
                    dep = instantiateDepartment(resultSet);
                    deps.put(resultSet.getInt("DepartmentId"), dep);
                }

                sellers.add(instantiateSeller(resultSet, dep));
            }
            return sellers;
        }
        catch (SQLException e){
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeStatement(preparedStatement);
            DB.closeResultSet(resultSet);
        }
    }

    @Override
    public List<Seller> findByDepartment(Department department) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Seller> sellers = new ArrayList<>();

        try {
            preparedStatement = connection.prepareStatement(
                    "SELECT seller.*,department.Name as DepName " +
                            "FROM seller INNER JOIN department " +
                            "ON seller.DepartmentId = department.Id " +
                            "WHERE DepartmentId = ? " +
                            "ORDER BY Name"
            );

            preparedStatement.setInt(1, department.getId());

            resultSet = preparedStatement.executeQuery();
            Map<Integer, Department> map = new HashMap<>();

            while (resultSet.next()){
                Department dep = map.get(resultSet.getInt("DepartmentId"));

                if (dep == null){
                    dep = instantiateDepartment(resultSet);
                    map.put(resultSet.getInt("DepartmentId"), dep);
                }

                sellers.add(instantiateSeller(resultSet, dep));
            }

            return sellers;
        }
        catch (SQLException e){
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeStatement(preparedStatement);
            DB.closeResultSet(resultSet);
        }
    }
}
