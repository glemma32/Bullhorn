package byaj.models;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by student on 6/27/17.
 */
@Entity
public class Search {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    //@NotNull
    //@Min(1)
    private int searchID;

    private String searchValue;

    private String searchType;
    @Column(columnDefinition="integer default -1")
    private int searchRes;


    public int getSearchID() {
        return searchID;
    }

    /*public void setMateID(int searchID) {
        this.searchID = searchID;
    }*/
    public String getSearchValue() {
        return searchValue;
    }

    public void setSearchValue (String searchValue) {
        this.searchValue = searchValue;
    }

    public String getSearchType() {
        return searchType;
    }

    public void setSearchType (String searchType) {
        this.searchType = searchType;
    }


    public int getSearchRes() {
        return searchRes;
    }

    public void setSearchRes (int searchRes) {
        this.searchRes = searchRes;
    }
}
