package com.gmail.cruvix.arearegenerator;

import org.bukkit.Material;

import java.sql.*;
import java.util.ArrayList;

public class DatabaseManager {

    /**
     * Private method to get a connection object belongs to the AreaRegenerator database.
     * @return a connection object if the database exists or has been successfully created.
     */
    private static Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + AreaRegenerator.getProperties().getProperty("resourcesPath") + "\\AreaRegenerator.db");
            //This request indicates to SQLite database to activate foreign key constraint.
            String foreignKeyEnableRequest = "PRAGMA foreign_keys = ON";
            PreparedStatement statement = connection.prepareStatement(foreignKeyEnableRequest);
            statement.execute();
            statement.close();
            return connection;
        }
        catch (SQLException e) {
            return null;
        }
    }

    /**
     * Method to create all tables needed into the AreaRegenerator database to store all data, only if they did not exist.
     */
    public static void createDatabase() {
        String areasTableRequest =
                //An area is described by a unique name, a world name where it belongs to, a first corner point
                //and an opposite second corner point. The two corners must be unique, to avoid two areas to be riding.
                "CREATE TABLE IF NOT EXISTS Areas" +
                "(" +
                    "areaID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                    "name VARCHAR(64) UNIQUE NOT NULL," +
                    "worldName VARCHAR(64) NOT NULL," +
                    "point1X INTEGER," +
                    "point1Y INTEGER," +
                    "point1Z INTEGER," +
                    "point2X INTEGER," +
                    "point2Y INTEGER," +
                    "point2Z INTEGER," +
                    "CONSTRAINT point1 UNIQUE(point1X,point1Y,point1Z)," +
                    "CONSTRAINT point2 UNIQUE(point2X,point2Y,point2Z)," +
                    "CHECK (point1X != point2X AND point1Z != point2Z)" +
                ")";
        //A placeable block is only described by a material type.
        String placeableBlocksTableRequest =
                "CREATE TABLE IF NOT EXISTS PlaceableBlocks" +
                "(" +
                    "materialType VARCHAR(64) PRIMARY KEY NOT NULL" +
                ")";
        //A non-explosive block is only described by a material type.
        String nonExplosiveBlocksTableRequest =
                "CREATE TABLE IF NOT EXISTS NonExplosiveBlocks" +
                "(" +
                    "materialType VARCHAR(64) PRIMARY KEY NOT NULL" +
                ")";
        //Table that represents a many-to-many relation between an area and placeable blocks in this area.
        String containsPlaceableBlocksTableRequest =
                "CREATE TABLE IF NOT EXISTS ContainsPlaceableBlocks" +
                "(" +
                    "areaID INTEGER NOT NULL," +
                    "materialType VARCHAR(64) NOT NULL," +
                    "PRIMARY KEY (areaID,materialType)," +
                    "FOREIGN KEY (areaID) REFERENCES Areas(areaID) ON DELETE CASCADE," +
                    "FOREIGN KEY (materialType) REFERENCES PlaceableBlocks(materialType)" +
                ")";
        //Table that represents a many-to-many relation between an area and non-explosive blocks in this area
        String containsNonExplosiveBlocksTableRequest =
                "CREATE TABLE IF NOT EXISTS ContainsNonExplosiveBlocks" +
                "(" +
                    "areaID INTEGER NOT NULL," +
                    "materialType VARCHAR(64) NOT NULL," +
                    "PRIMARY KEY (areaID,materialType)," +
                    "FOREIGN KEY (areaID) REFERENCES Areas(areaID) ON DELETE CASCADE," +
                    "FOREIGN KEY (materialType) REFERENCES NonExplosiveBlocks(materialType)" +
                ")";
        Connection connection = getConnection();
        PreparedStatement statement = null;

        try {
            //Execution of all previous requests to create tables in the AreaRegenerator database.
            statement = connection.prepareStatement(areasTableRequest);
            statement.executeUpdate();

            statement = connection.prepareStatement(placeableBlocksTableRequest);
            statement.executeUpdate();

            statement = connection.prepareStatement(nonExplosiveBlocksTableRequest);
            statement.executeUpdate();

            statement = connection.prepareStatement(containsPlaceableBlocksTableRequest);
            statement.executeUpdate();

            statement = connection.prepareStatement(containsNonExplosiveBlocksTableRequest);
            statement.executeUpdate();
        }
        catch (SQLException | NullPointerException e) {}
        finally {
            try {
                statement.close();
                connection.close();
            }
            catch (SQLException | NullPointerException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Insert an AreaInformation into the AreaRegenerator database.
     * @param areaInformation the information about the area to insert.
     */
    public static void insertArea(AreaInformation areaInformation) {
        String name = areaInformation.getAreaName();
        String worldName = areaInformation.getWorldName();
        Coordinate point1 = areaInformation.getPoint1();
        Coordinate point2 = areaInformation.getPoint2();
        int point1X = point1.getX();
        int point1Y = point1.getY();
        int point1Z = point1.getZ();
        int point2X = point2.getX();
        int point2Y = point2.getY();
        int point2Z = point2.getZ();

        String insertAreaRequest = "INSERT INTO Areas (name,worldName,point1X,point1Y,point1Z,point2X,point2Y,point2Z)" +
                "VALUES (?,?,?,?,?,?,?,?)";

        Connection connection = getConnection();
        PreparedStatement statement = null;

        try {
            statement = connection.prepareStatement(insertAreaRequest);
            statement.setString(1, name);
            statement.setString(2, worldName);
            statement.setInt(3, point1X);
            statement.setInt(4, point1Y);
            statement.setInt(5, point1Z);
            statement.setInt(6, point2X);
            statement.setInt(7, point2Y);
            statement.setInt(8, point2Z);
            statement.executeUpdate();
        }
        catch (SQLException | NullPointerException e) {}
        finally {
            try {
                statement.close();
                connection.close();
            } catch (SQLException | NullPointerException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Insert non-explosive or placeable blocks (according to blockMode) into the database for a specified area.
     * @param areaInformation the information about the area.
     * @param materials the materials list to add for this area.
     * @param blockMode choose if these materials are non-explosive or placeable.
     */
    public static void insertBlocks(AreaInformation areaInformation, ArrayList<Material> materials, BlockMode blockMode) {
        //The first parameter in the first request is PlaceableBlocks or NonExplosiveBlocks according to the value
        //of blockMode. In the second request, the first parameter is ContainsPlaceableBlocks or ContainsNonExplosiveBlocks
        //according to the value of blockMode.
        String insertPlaceableBlocksRequest = "INSERT INTO ? (materialType) VALUES (?);" +
                "INSERT INTO ? (areaID,materialType) VALUES (?,?)";

        Connection connection = getConnection();
        PreparedStatement statement = null;

        try {
            int areaID = getAreaIDByName(areaInformation);

            statement = connection.prepareStatement(insertPlaceableBlocksRequest);

            if (blockMode.equals(BlockMode.PLACEABLE)) {
                statement.setString(1,"PlaceableBlocks");
                statement.setString(3,"ContainsPlaceableBlocks");
                materials = areaInformation.getPlaceableBlocks();
            }
            else if (blockMode.equals(BlockMode.NON_EXPLOSIVE)){
                statement.setString(1,"NonExplosiveBlocks");
                statement.setString(3,"ContainsNonExplosiveBlocks");
                materials = areaInformation.getNonExplosiveBlocks();
            }

            statement.setInt(4,areaID);
            for (Material material : materials) {
                statement.setString(2,material.toString());
                statement.setString(5,material.toString());
                statement.addBatch();
                statement.clearParameters();
            }
            statement.executeBatch();
            statement.executeBatch();
        }
        catch (SQLException | NullPointerException e) {}
        finally {
            try {
                statement.close();
                connection.close();
            } catch (SQLException | NullPointerException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Drop an area into the AreaRegenerator database.
     * @param areaInformation the information about the area to drop.
     */
    public static void dropArea(AreaInformation areaInformation) {
        String name = areaInformation.getAreaName();
        String dropAreaInformationRequest = "DELETE FROM Areas WHERE name = ?";
        String dropBlocksIntoDeletedAreaRequest = "DELETE FROM PlaceableBlocks WHERE materialType NOT IN (SELECT materialType FROM ContainsPlaceableBlocks);" +
                "DELETE FROM NonExplosiveBlocks WHERE materialType NOT IN (SELECT materialType FROM ContainsNonExplosiveBlocks)";

        Connection connection = getConnection();
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(dropAreaInformationRequest);
            statement.setString(1,name);
            statement.executeUpdate();

            statement = connection.prepareStatement(dropBlocksIntoDeletedAreaRequest);
            statement.executeUpdate();
        }
        catch (SQLException | NullPointerException e) {}
        finally {
            try {
                statement.close();
                connection.close();
            } catch (SQLException | NullPointerException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Drop non-explosive or placeable blocks (according to blockMode) into the database for a specified area.
     * @param areaInformation the information about the area.
     * @param materials the list of material to drop for this area.
     * @param blockMode choose if these materials are non-explosive or placeable.
     */
    public static void dropBlocks(AreaInformation areaInformation, ArrayList<Material> materials, BlockMode blockMode) {
        String dropBlocksRequest = "DELETE FROM ? WHERE ?.areaID = ? AND " +
                "?.materialType = ?; DELETE FROM ? WHERE materialType NOT IN (SELECT materialType FROM ?)";

        Connection connection = getConnection();
        PreparedStatement statement = null;

        try {
            int areaId = getAreaIDByName(areaInformation);

            statement = connection.prepareStatement(dropBlocksRequest);
            if (blockMode.equals(BlockMode.PLACEABLE)) {
                statement.setString(1,"ContainsPlaceableBlocks");
                statement.setString(2,"ContainsPlaceableBlocks");
                statement.setInt(3,areaId);
                statement.setString(4,"ContainsPlaceableBlocks");
                statement.setString(6,"PlaceableBlocks");
                statement.setString(7,"ContainsPlaceableBlocks");
            }
            else if (blockMode.equals(BlockMode.NON_EXPLOSIVE)){
                statement.setString(1,"ContainsNonExplosiveBlocks");
                statement.setString(2,"ContainsNonExplosiveBlocks");
                statement.setInt(3,areaId);
                statement.setString(4,"ContainsNonExplosiveBlocks");
                statement.setString(6,"NonExplosiveBlocks");
                statement.setString(7,"ContainsNonExplosiveBlocks");
            }
            for (Material material : materials) {
                statement.setString(5,material.toString());
                statement.addBatch();
            }
            statement.executeBatch();
        }
        catch (SQLException | NullPointerException e) {}
        finally {
            try {
                statement.close();
                connection.close();
            }
            catch (SQLException | NullPointerException e) {
                e.printStackTrace();
            }
        }
    }

    public static ArrayList<AreaInformation> getAllAreas() {
        String getAllAreasRequest = "SELECT * FROM Areas";
        String getPlaceableBlocksRequest = "SELECT ContainsPlaceableBlocks.materialType FROM ContainsPlaceableBlocks WHERE ContainsPlaceableBlocks.areaID = ?";
        String getNonExplosiveBlocksRequest = "SELECT ContainsNonExplosiveBlocks.materialType FROM ContainsNonExplosiveBlocks WHERE ContainsNonExplosiveBlocks.areaID = ?";

        Connection connection = getConnection();
        PreparedStatement statement = null;

        try {
            statement = connection.prepareStatement(getAllAreasRequest);
            ResultSet resultSet = statement.executeQuery();

            int areaID = 0;
            String name = "";
            String worldName = "";
            int point1X = 0;
            int point1Y = 0;
            int point1Z = 0;
            int point2X = 0;
            int point2Y = 0;
            int point2Z = 0;

            while (resultSet.next()) {
                areaID = resultSet.getInt(1);
                name = resultSet.getString(2);
                worldName = resultSet.getString(3);
                point1X = resultSet.getInt(4);
                point1Y = resultSet.getInt(5);
                point1Z = resultSet.getInt(6);
                point2X = resultSet.getInt(7);
                point2Y = resultSet.getInt(8);
                point2Z = resultSet.getInt(9);
                AreaInformation areaInformation = new AreaInformation(name,worldName,new Coordinate(point1X,point1Y,point1Z),new Coordinate(point2X,point2Y,point2Z));
                statement = connection.prepareStatement(getPlaceableBlocksRequest);
                statement.setInt(1,areaID);
                ResultSet blocks = statement.executeQuery();
                while (blocks.next()) {
                    areaInformation.addPlaceableMaterial(Material.matchMaterial(blocks.getString(1)));
                }
                statement = connection.prepareStatement(getNonExplosiveBlocksRequest);
                statement.setInt(1,areaID);
                blocks = statement.executeQuery();
                while (blocks.next()) {
                    areaInformation.addNonExplosiveMaterial(Material.matchMaterial(blocks.getString(1)));
                }
                AreaRegister.getInstance().addAreaInformations(areaInformation);
            }
        }
        catch (SQLException | NullPointerException e) {}
        finally {
            try {
                statement.close();
                connection.close();
            }
            catch (SQLException | NullPointerException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private static int getAreaIDByName(AreaInformation areaInformation) {
        String name = areaInformation.getAreaName();
        String selectIDRequest = "SELECT Areas.areaID FROM Areas WHERE Areas.name = ?";

        Connection connection = getConnection();
        PreparedStatement statement = null;

        try {
            statement = connection.prepareStatement(selectIDRequest);
            statement.setString(1,name);
            int areaID = statement.executeQuery().getInt(1);
            return areaID;
        }
        catch (SQLException | NullPointerException e) {return 0;}
        finally {
            try {
                statement.close();
                connection.close();
            }
            catch (SQLException | NullPointerException e) {
                e.printStackTrace();
                return 0;
            }
        }
    }
}
