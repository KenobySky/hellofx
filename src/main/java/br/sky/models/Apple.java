package br.sky.models;

import javax.persistence.*;
import org.hibernate.envers.Audited;


/**
 *
 * @author Andre Lopes
 */


@Audited
@Entity
public class Apple extends Fruit{

    public Apple() {
    }

    public Apple(long id, String color) {
        super(id, color);
    }

    
    
    @Override
    public void setColor(String color) {
        super.setColor(color); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getColor() {
        return super.getColor(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setId(long id) {
        super.setId(id); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public long getId() {
        return super.getId(); //To change body of generated methods, choose Tools | Templates.
    }





    
}
