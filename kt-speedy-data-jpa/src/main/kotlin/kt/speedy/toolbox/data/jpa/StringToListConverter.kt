package kt.speedy.toolbox.data.jpa

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

@Converter
class StringToListConverter : AttributeConverter<String, List<String>> {
    override fun convertToDatabaseColumn(attribute: String?): List<String> {
        return attribute?.split(",") ?: emptyList()
    }

    override fun convertToEntityAttribute(list: List<String>?): String {
        return list?.joinToString(",") ?: ""
    }
}