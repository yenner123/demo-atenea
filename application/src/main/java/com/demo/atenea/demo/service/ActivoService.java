package com.demo.atenea.demo.service;

import java.util.List;
import java.util.Optional;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.transaction.Transactional;

import com.demo.atenea.demo.domain.Activo;
import com.demo.atenea.demo.domain.TipoActivo;
import com.demo.atenea.demo.dto.ActivoDTO;
import com.demo.atenea.demo.dto.ActivoSearchCriteriaDTO;
import com.demo.atenea.demo.mapper.ActivoMapper;
import com.demo.atenea.demo.repository.ActivoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class ActivoService {

    @Autowired
    private ActivoRepository repository;

    /**
     * Get list actives
     * 
     * @return list actives
     */
    public Optional<List<ActivoDTO>> getAll() {
        Optional<List<ActivoDTO>> dtos = Optional.empty();
        List<Activo> listActives = repository.findAll();
        if (!listActives.isEmpty()) {
            dtos = Optional.ofNullable(ActivoMapper.toDTO(listActives));
        }
        return dtos;
    }

    /**
     * Get active by id
     * 
     * @param id
     * @return active
     */
    public Optional<ActivoDTO> get(long id) {
        return repository.findById(id)
                .map(entity -> Optional.of(ActivoMapper.toDTO(entity)))
                .orElseGet(() -> Optional.empty());
    }

    /**
     * Get actives by criteria filters
     * 
     * @param criterios
     * @return
     */
    public Optional<List<ActivoDTO>> findByCriterias(ActivoSearchCriteriaDTO criterios) {
        Optional<List<ActivoDTO>> dtos = Optional.empty();
        List<Activo> listActives = repository.findAll(createSpecification(criterios));
        if (!listActives.isEmpty()) {
            dtos = Optional.ofNullable(ActivoMapper.toDTO(listActives));
        }
        return dtos;
    }

    /**
     * Save active
     * 
     * @param activoDTO
     */
    public ActivoDTO save(ActivoDTO activoDTO) {
        Activo aspirante = ActivoMapper.toEntity(activoDTO);
        return ActivoMapper.toDTO(repository.save(aspirante));
    }

    /**
     * Patch active
     * 
     * @param activoDTO
     */
    public Optional<ActivoDTO> patch(Long id, ActivoDTO activoDTO) {
        return repository.findById(id)
                .map(entity -> {
                    entity.setSerial(activoDTO.getSerial());
                    entity.setFechaBaja(activoDTO.getFechaBaja());
                    return Optional.of(ActivoMapper.toDTO(entity));
                })
                .orElseGet(() -> Optional.empty());
    }

    /**
     * Delete active by id
     * 
     * @param id
     */
    public void delete(long id) {
        repository.deleteById(id);
    }

    private Specification<Activo> createSpecification(ActivoSearchCriteriaDTO criteria) {
        Specification<Activo> specification = Specification.where(null);

        if (criteria != null) {
            if (criteria.getTipo() != null) {
                specification = specification.and((root, query, builder) -> {
                    Join<Activo, TipoActivo> tipoActivoJoin = root.join("tipoActivo", JoinType.INNER);
                    return builder.like(tipoActivoJoin.get("nombre"), "%" + criteria.getTipo() + "%");
                });
            }

            if (criteria.getSerial() != null) {
                specification = specification.and(
                        (root, query, builder) -> builder.like(root.get("serial"), "%" + criteria.getSerial() + "%"));
            }

            if (criteria.getFechaCompra() != null) {
                specification = specification.and(
                        (root, query, builder) -> builder.equal(root.get("fechaCompra"), criteria.getFechaCompra()));
            }
        }
        return specification;
    }
}
