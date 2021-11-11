package pl.com.seremak.fileWriter

import pl.com.seremak.Util.ResultFileWriter
import spock.lang.Specification


class FileWriterTest extends Specification{

    def 'should crate file path'() {

        given:
        def fileWriter = new ResultFileWriter(false)

        when:
        def fileName = fileWriter.createFileName()

        then:
        fileName.toString().matches("^SGA_result_\\d+_\\d+\\.txt")
    }
}
