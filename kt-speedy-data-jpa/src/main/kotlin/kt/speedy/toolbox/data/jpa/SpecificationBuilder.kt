package kt.speedy.toolbox.data.jpa

import org.springframework.data.jpa.domain.Specification

/**
 * Specification 扩展类
 */
class SpecificationBuilder<T> private constructor() {
    private lateinit var spec: Specification<T>

    companion object {
        fun <T> builder(): SpecificationBuilder<T> {
            return SpecificationBuilder<T>().apply {
                spec = Specification.where(null)
            }
        }
    }

    fun and(newSpec: Specification<T>): SpecificationBuilder<T> {
        spec = spec.and(newSpec)
        return this
    }
    fun or(newSpec: Specification<T>): SpecificationBuilder<T> {
        spec = spec.or(newSpec)
        return this
    }

    fun build(): Specification<T> {
        return spec
    }
}