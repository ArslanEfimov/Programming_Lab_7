package shared.core.models;


import shared.core.Exceptions.IncorrectValueException;

import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

/**
 * Organization class Coordinates
 */
@XmlType(propOrder = {"x","y"})
public class Coordinates implements Serializable {
        private Float x;
        private int y;
        private final int MIN_Y = -98;

        public Coordinates(){

        }
        public Coordinates(Float x, int y){

            this.x = x;
            this.y = y;

        }
        public Float getX() {
            return x;
        }

    /**
     *
     * @param x
     * @throws IncorrectValueException
     */
    public void setX(Float x) throws IncorrectValueException {
            if(x == null){
                throw new IncorrectValueException("координата x не может быть (null)");
            }
            this.x = x;
        }

        public int getY() {
            return y;
        }

    /**
     *
     * @param y
     * @throws IncorrectValueException
     */
    public void setY(int y) throws IncorrectValueException {
            if((y<=MIN_Y && (y!=0))){
                this.y = y;
                throw new IncorrectValueException("координата y должна быть больше -98");
            }
                this.y = y;

        }

    /**
     *
     * @return x and y Coordinates
     */
    @Override
        public String toString() {
            return "Coordinates{" +
                    "x=" + x +
                    ", y=" + y +
                    '}';
        }


}
