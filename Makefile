# Makefile for triangulation app
# Prerequisites:
# 	java >= 1.8
#	maven >= 3.0

TRIANGULATION = triangulation
JAR = target/triangulation-*.jar
BIN = bin/
TARGET = $(BIN)$(TRIANGULATION)

$(TRIANGULATION):  setup
	@mvn package
	@cp $(JAR) $(TARGET)
	@chmod +x $(TARGET)

run: $(TRIANGULATION)
	@$(TARGET)

clean:
	@mvn clean
	@rm -rf $(BIN)
	@rm dependency-reduced-pom.xml

setup:
	@mkdir -p $(BIN)
