package gajudama.javematch.logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gajudama.javematch.accesoDatos.InteresRepository;
import gajudama.javematch.accesoDatos.PlanRepository;
import gajudama.javematch.accesoDatos.UsuarioRepository;
import gajudama.javematch.model.Amistad;
import gajudama.javematch.model.Interes;
import gajudama.javematch.model.Plan;
import gajudama.javematch.model.Rechazo;
import gajudama.javematch.model.UserLike;
import gajudama.javematch.model.UserMatch;
import gajudama.javematch.model.Usuario;
import jakarta.transaction.Transactional;

@Service
public class UsuarioLogic {
    @Autowired
    private UsuarioRepository usuarioRepository;


    @Transactional
    public Usuario createUsuario(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public Optional<Usuario> getUsuarioById(Long id) {
        return usuarioRepository.findById(id);
    }

    @Transactional
    public Usuario updateUsuario(Long id, Usuario usuarioDetails) {
        return usuarioRepository.findById(id).map(usuario -> {
            usuario.setNombre(usuarioDetails.getNombre());
            usuario.setCorreo(usuarioDetails.getCorreo());
            usuario.setPlan(usuarioDetails.getPlan());
            return usuarioRepository.save(usuario);
        }).orElseThrow(() -> new RuntimeException("Usuario not found"));
    }

    @Transactional
    public void deleteUsuario(Long id) {
        usuarioRepository.deleteById(id);
    }

    public List<Usuario> getAllUsuarios() {
        return usuarioRepository.findAll();
    }

    // Nuevos métodos para actualizar listas específicas
    @Transactional
    public Usuario updateLikesGiven(Long id, List<UserLike> likesGiven) {
        return usuarioRepository.findById(id).map(usuario -> {
            usuario.setLikesGiven(likesGiven);
            return usuarioRepository.save(usuario);
        }).orElseThrow(() -> new RuntimeException("Usuario not found"));
    }

    @Transactional
    public Usuario updateLikesReceived(Long id, List<UserLike> likesReceived) {
        return usuarioRepository.findById(id).map(usuario -> {
            usuario.setLikesReceived(likesReceived);
            return usuarioRepository.save(usuario);
        }).orElseThrow(() -> new RuntimeException("Usuario not found"));
    }

    @Transactional
    public Usuario updateMatchesAsUser1(Long id, List<UserMatch> matchesAsUser1) {
        return usuarioRepository.findById(id).map(usuario -> {
            usuario.setMatchesAsUser1(matchesAsUser1);
            return usuarioRepository.save(usuario);
        }).orElseThrow(() -> new RuntimeException("Usuario not found"));
    }

    @Transactional
    public Usuario updateMatchesAsUser2(Long id, List<UserMatch> matchesAsUser2) {
        return usuarioRepository.findById(id).map(usuario -> {
            usuario.setMatchesAsUser2(matchesAsUser2);
            return usuarioRepository.save(usuario);
        }).orElseThrow(() -> new RuntimeException("Usuario not found"));
    }

    @Transactional
    public Usuario updateFriendshipsAsUser(Long id, List<Amistad> friendshipsAsUser) {
        return usuarioRepository.findById(id).map(usuario -> {
            usuario.setFriendshipsAsUser(friendshipsAsUser);
            return usuarioRepository.save(usuario);
        }).orElseThrow(() -> new RuntimeException("Usuario not found"));
    }

    @Transactional
    public Usuario updateFriendshipsAsFriend(Long id, List<Amistad> friendshipsAsFriend) {
        return usuarioRepository.findById(id).map(usuario -> {
            usuario.setFriendshipsAsFriend(friendshipsAsFriend);
            return usuarioRepository.save(usuario);
        }).orElseThrow(() -> new RuntimeException("Usuario not found"));

    }

    @Transactional
    public Usuario updateRejectionsGiven(Long id, List<Rechazo> usuarioRechazo) {
        return usuarioRepository.findById(id).map(usuario -> {
            usuario.setRejectionsGiven(usuarioRechazo);
            return usuarioRepository.save(usuario);
        }).orElseThrow(() -> new RuntimeException("Usuario not found"));
    }

    @Transactional
    public Usuario updateRejectionsReceived(Long id, List<Rechazo> rechazoUsuario) {
        return usuarioRepository.findById(id).map(usuario -> {
            usuario.setRejectionsReceived(rechazoUsuario);
            return usuarioRepository.save(usuario);
        }).orElseThrow(() -> new RuntimeException("Usuario not found"));

    }

    @Autowired
    private PlanRepository PlanRepository;

    @Autowired
    private InteresRepository InteresRepository;
    
   @Transactional
    public Usuario registerUsuario(Usuario usuario) {
        System.out.println("Comenzando el registro del usuario: " + usuario);

        // Validar dominio del correo
        if (!usuario.getCorreo().endsWith("@javeriana.edu.co")) {
            System.out.println("Error: El correo debe ser de dominio @javeriana.edu.co");
            throw new IllegalArgumentException("El correo debe ser de dominio @javeriana.edu.co");
        }

        // Verificar y asignar el plan
        System.out.println("Buscando plan con ID: " + usuario.getPlan().getPlan_id());
        Plan plan = PlanRepository.findById(usuario.getPlan().getPlan_id())
                .orElseThrow(() -> new IllegalArgumentException("Plan no encontrado"));
        usuario.setPlan(plan);
        System.out.println("Plan asignado correctamente: " + plan);

        // Procesar y asignar los intereses del usuario
        List<Interes> processedIntereses = new ArrayList<>();
        for (Interes interes : usuario.getIntereses()) {
            System.out.println("Procesando interés: " + interes.getNombre());
            Optional<Interes> foundInteres = InteresRepository.findByNombre(interes.getNombre());

            if (foundInteres.isPresent()) {
                processedIntereses.add(foundInteres.get());
            } else {
                // Si el interés no existe, guardarlo en la base de datos
                processedIntereses.add(InteresRepository.save(interes));
                System.out.println("Nuevo interés guardado: " + interes.getNombre());
            }
        }
        usuario.setIntereses(processedIntereses);

        // Guardar el usuario en el repositorio y devolverlo
        System.out.println("Guardando el usuario: " + usuario);
        return usuarioRepository.save(usuario);
    }

    public Optional<Usuario> loginUsuario(String correo) {
        return usuarioRepository.findByCorreo(correo);
    }

    @Transactional
    public Usuario upgradePlan(Long usuarioId, Plan newPlan) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
            .orElseThrow(() -> new RuntimeException("User not found"));

        usuario.setPlan(newPlan);
        return usuarioRepository.save(usuario);
    }

    @Transactional
    public Usuario addInteres(Long usuarioId, Interes interes) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
            .orElseThrow(() -> new RuntimeException("User not found"));

        usuario.getIntereses().add(interes);
        return usuarioRepository.save(usuario);
    }

    public List<Usuario> findUsersWithMatchingInterests(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
            .orElseThrow(() -> new RuntimeException("User not found"));

        return usuarioRepository.findByInteresesIn(usuario.getIntereses());
    }

    public Usuario getRandomUsuario() {
        List<Usuario> usuarios = usuarioRepository.findAll(); // Obtener todos los usuarios
        if (usuarios.isEmpty()) {
            return null; // Retornar null si no hay usuarios
        }
        Random rand = new Random();
        return usuarios.get(rand.nextInt(usuarios.size())); // Seleccionar un usuario aleatorio
    }
    
}
