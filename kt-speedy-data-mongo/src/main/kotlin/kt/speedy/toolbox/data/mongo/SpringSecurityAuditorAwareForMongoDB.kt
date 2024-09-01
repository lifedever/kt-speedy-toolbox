package kt.speedy.toolbox.data.mongo

import org.springframework.data.domain.AuditorAware
import java.util.*

/**
 * 用于jpa自动生成创建人和更新人信息
 */
abstract class SpringSecurityAuditorAwareForMongoDB : AuditorAware<String> {

    abstract override fun getCurrentAuditor(): Optional<String>
}