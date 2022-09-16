//package me.toelke.pnpmusicapp.api.mongodb
//
//import com.mongodb.reactivestreams.client.MongoClient
//import com.mongodb.reactivestreams.client.MongoClients
//import me.toelke.pnpmusicapp.api.song.SongRepository
//import org.springframework.context.annotation.Bean
//import org.springframework.context.annotation.Configuration
//import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration
//import org.springframework.data.mongodb.core.ReactiveMongoTemplate
//import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories
//
//@Configuration
//@EnableReactiveMongoRepositories(basePackageClasses = [
//    SongRepository::class
//])
//class MongoConfig(
//    private val properties: MongoProperties
//): AbstractReactiveMongoConfiguration() {
//    override fun getDatabaseName(): String = "pnpmusicapp"
//
//    override fun reactiveMongoClient(): MongoClient = mongoClient()
//
//    private val connection = ""
//
//    @Bean
//    fun mongoClient() = MongoClients.create(connection)
//
//    @Bean
//    fun reactiveMongoTemplate() = ReactiveMongoTemplate(mongoClient(), databaseName)
//}