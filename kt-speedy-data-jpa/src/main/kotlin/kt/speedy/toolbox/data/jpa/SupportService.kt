package kt.speedy.toolbox.data.jpa

import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.repository.findByIdOrNull
import org.springframework.transaction.annotation.Transactional
import java.io.Serializable
import java.util.*

@Transactional(readOnly = true)
abstract class SupportService<T : SupportModal, ID : Serializable> {
    protected abstract val repository: SupportRepository<T, ID>

    @PersistenceContext
    protected var entityManager: EntityManager? = null
    fun findById(id: ID): Optional<T> {
        return repository.findById(id)
    }

    fun findAllById(ids: MutableList<ID>): MutableList<T> {
        return repository.findAllById(ids)
    }

    fun getOne(id: ID): T {
        return repository.findById(id).orElseThrow { Exception("Data not found with id: $id") }
    }

    fun getOneOrNull(id: ID?): T? {
        return if (id == null) null else repository.findById(id).orElse(null)
    }

    @Transactional
    open fun save(t: T): T {
        return repository.save(t)
    }

    @Transactional
    open fun saveAll(entities: List<T>): List<T> {
        return repository.saveAll(entities)
    }

    fun findAll(): List<T> {
        return repository.findAll()
    }

    fun findAll(sort: Sort): List<T> {
        return repository.findAll(sort)
    }

    fun findAll(spec: Specification<T>): List<T> {
        return repository.findAll(spec)
    }

    fun findAll(spec: Specification<T>, pageable: Pageable): Page<T> {
        return repository.findAll(spec, pageable)
    }

    fun findAll(spec: Specification<T>, sort: Sort): List<T> {
        return repository.findAll(spec, sort)
    }

    fun findAll(pageable: Pageable): Page<T> {
        return repository.findAll(pageable)
    }

    @Transactional
    open fun deleteById(id: ID) {
        repository.deleteById(id)
    }

    @Transactional
    open fun delete(entity: T) {
        repository.delete(entity)
    }

    @Transactional
    open fun deleteAll() {
        repository.deleteAll()
    }

    @Transactional
    open fun deleteAllById(ids: List<ID>) {
        repository.deleteAllById(ids)
    }

    @Transactional
    open fun deleteAllInBatch(entities: List<T>) {
        repository.deleteAllInBatch(entities)
    }

    /**
     * 逻辑删除
     */
    @Transactional
    open fun deleteLogic(id: ID) {
        repository.findByIdOrNull(id)?.let {
            it.deleted = true
            this.save(it)
        }
    }

    fun count(): Long {
        return repository.count()
    }

    fun count(spec: Specification<T>): Long {
        return repository.count(spec)
    }
}