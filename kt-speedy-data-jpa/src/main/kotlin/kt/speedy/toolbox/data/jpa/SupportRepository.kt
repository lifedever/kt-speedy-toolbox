package kt.speedy.toolbox.data.jpa

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.repository.NoRepositoryBean
import java.io.Serializable

@NoRepositoryBean
interface SupportRepository<T, ID : Serializable> : JpaRepository<T, ID>, JpaSpecificationExecutor<T>