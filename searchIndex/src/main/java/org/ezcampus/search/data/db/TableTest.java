package org.ezcampus.search.data.db;

public class TableTest {

	
    public static final String DELETION_QUERY = "DROP TABLE IF EXISTS tbl_file";
    public static final String CREATION_QUERY = "CREATE TABLE IF NOT EXISTS tbl_file("
            + "hash_id serial PRIMARY KEY, "
            + "size BIGINT NOT NULL, "
            + "mime INT NOT NULL,"
            + "width INT NOT NULL, "
            + "height INT NOT NULL,"
            + "duration INT NOT NULL,"
            + "has_audio BOOLEAN NOT NULL"
        
            + ");";
    

	
}
