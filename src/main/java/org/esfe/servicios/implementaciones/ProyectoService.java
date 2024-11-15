    package org.esfe.servicios.implementaciones;

    import org.esfe.modelos.Proyecto;
    import org.esfe.modelos.Usuario;
    import org.esfe.repositorio.IProyectoRepository; // Asegúrate de que este repositorio esté definido
    import org.esfe.repositorio.IUsuarioRepository;
    import org.esfe.servicios.interfaces.IProyectoService;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.data.domain.Page;
    import org.springframework.data.domain.Pageable;
    import org.springframework.stereotype.Service;

    import java.util.Collections;
    import java.util.List;
    import java.util.Optional;

    @Service
    public class ProyectoService implements IProyectoService {

        @Autowired
        private IProyectoRepository proyectoRepository;

        @Autowired
        private IUsuarioRepository usuarioRepository;

        @Override
        public Page<Proyecto> buscarTodosLospaginado(Pageable pageable) {
            return proyectoRepository.findAll(pageable);
        }

        @Override
        public List<Proyecto> ObtenerTodos() {
            return proyectoRepository.findAll();
        }

        @Override
        public Optional<Proyecto> buscarPorId(Integer proyectoId) {
            return proyectoRepository.findById(proyectoId);
        }

        @Override
        public Proyecto crearOEditar(Proyecto proyecto) {
            return proyectoRepository.save(proyecto);
        }

        @Override
        public void eliminarPorid(Integer proyectoId) {
            Proyecto proyecto = proyectoRepository.findById(proyectoId)
                    .orElseThrow(() -> new RuntimeException("Proyecto no encontrado"));


            proyectoRepository.delete(proyecto);
        }


        @Override
        public List<Proyecto> getProyectosByUsuario(Usuario usuario) {
            if (usuario != null) {
                return proyectoRepository.findByUsuario(usuario);
            } else {
                return Collections.emptyList();
            }
        }

        @Override
        public List<Proyecto> obtenerProyectosCompletados() {
            return proyectoRepository.findByEstado("completado");
        }

        @Override
        public List<Proyecto> obtenerProyectosActivos() {
            return proyectoRepository.findByEstado("activo");
        }

        @Override
        public List<Proyecto> obtenerProyectosCancelados() {
            return proyectoRepository.findByEstado("cancelado");
        }

        @Override
        public List<Proyecto> findAllOrderedByNombre() {
            return proyectoRepository.findAllByOrderByNombreAsc(); // Asegúrate de tener un método en el repositorio
        }

        @Override
        public List<Proyecto> findAllOrderedByFechaFin() {
            return proyectoRepository.findAllByOrderByFechaFinAsc(); // Asegúrate de tener un método en el repositorio
        }

        @Override
        public List<Proyecto> findAllOrderedByPresupuesto() {
            return proyectoRepository.findAllByOrderByPresupuestoAsc(); // Asegúrate de tener un método en el repositorio
        }


    }

