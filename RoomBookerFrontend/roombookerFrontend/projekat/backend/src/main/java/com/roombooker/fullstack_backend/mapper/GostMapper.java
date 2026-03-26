package com.roombooker.fullstack_backend.mapper;

import com.roombooker.fullstack_backend.dto.GostDTO;
import com.roombooker.fullstack_backend.dto.GradDTO;
import com.roombooker.fullstack_backend.model.Gost;
import com.roombooker.fullstack_backend.model.Grad;

import org.springframework.stereotype.Component;

@Component  // Ova anotacija oznacava da je ova klasa Spring komponenta.
// Spring ce je automatski registrovati i moci cemo je injectovati
// u druge klase pomoću @Autowired.
public class GostMapper implements BaseMapper<GostDTO, Gost> {

    @Override
    public Gost toDomainEntity(GostDTO gostDTO) {
        // Proveravamo da li je DTO null
        if (gostDTO == null) {
            return null;
        }

        // Kreiramo novi entitet Gost i prepisujemo podatke iz DTO-a
        Gost gost = new Gost();
        gost.setIdGost(gostDTO.getIdGost());
        gost.setIme(gostDTO.getIme());
        gost.setPrezime(gostDTO.getPrezime());
        gost.setEmail(gostDTO.getEmail());
        gost.setTelefon(gostDTO.getTelefon());
        gost.setAdresa(gostDTO.getAdresa());
        gost.setDatumRodjenja(gostDTO.getDatumRodjenja());
        gost.setLozinkaHash(gostDTO.getLozinka()); 

        // Ako DTO sadrzi podatke o gradu, pravimo novi Grad entitet
        if (gostDTO.getGrad() != null) {
            gost.setGrad(new Grad(
                gostDTO.getGrad().getId(),
                gostDTO.getGrad().getName(),
                gostDTO.getGrad().getPostanskiBroj()
            ));
        } else {
            gost.setGrad(null);
        }
        return gost;
    }

    @Override
    public GostDTO toDomainDTO(Gost gostEntity) {
        // Proveravamo da li je entitet null
        if (gostEntity == null) {
            return null;
        }

        // Kreiramo novi DTO i prepisujemo podatke iz entiteta.
        GostDTO gostDTO = new GostDTO();
        gostDTO.setIdGost(gostEntity.getIdGost());
        gostDTO.setIme(gostEntity.getIme());
        gostDTO.setPrezime(gostEntity.getPrezime());
        gostDTO.setEmail(gostEntity.getEmail());
        gostDTO.setTelefon(gostEntity.getTelefon());
        gostDTO.setAdresa(gostEntity.getAdresa());
        gostDTO.setDatumRodjenja(gostEntity.getDatumRodjenja());
        gostDTO.setLozinka(gostEntity.getLozinka()); 

        // Ako postoji grad, pravimo DTO verziju grada
        if (gostEntity.getGrad() != null) {
            gostDTO.setGrad(new GradDTO(
                gostEntity.getGrad().getIdGrad(),
                gostEntity.getGrad().getNaziv(),
                gostEntity.getGrad().getPostanskiBroj()
            ));
        }
        return gostDTO;
    }
}
