package pl.lotto.domain.numberreceiver;

public class HashGeneratorTestImpl implements HashGenerable {

    private final String hash;

    public HashGeneratorTestImpl() {
        hash = "123";
    }

    HashGeneratorTestImpl(String hash) {
        this.hash = hash;
    }

    @Override
    public String getHash() {
        return hash;
    }
}
