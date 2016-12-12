package ar.com.lacomarcasistemas.centrocristalesmobile.model;

import com.google.gson.Gson;

/**
 * Created by Test on 07/06/2016.
 */
public class EstadoOrdenTrabajo {
    public static final int OT_ENCONTRADA = 0;
    public static final int OT_NOENCONTRADA = -1;

    public static final String ESTADO_PASADO = "PAS";
    public static final String ESTADO_EN_TALLER = "ENT";
    public static final String ESTADO_TERMINADO = "TER";

    public String resultadoJson;
    public int status;
    public String statusText;
    public DetalleOrdenTrabajo detalleOrdenTrabajo;

    public void buildDetalleOrdenTrabajo()
    {
        DetalleOrdenTrabajo[] rawDes = new Gson().fromJson(this.resultadoJson, DetalleOrdenTrabajo[].class);
        this.detalleOrdenTrabajo = rawDes[0];
    }

    public class DetalleOrdenTrabajo{
        public int nroOT;
        public int id_presupuesto;
        public int version;
        public String observaciones;
        public String descripcionEstado;
        public String id_estado;
    }
}
