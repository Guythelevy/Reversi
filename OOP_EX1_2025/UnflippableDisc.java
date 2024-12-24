public class UnflippableDisc implements Disc {
    private Player owner;

    public UnflippableDisc(Player owner) {
        this.owner = owner;
    }

    @Override
    public Player getOwner() {
        return owner;
    }

    @Override
    public void setOwner(Player owner) {
        this.owner = owner;
    }

    @Override
    public String getType() {
        return "⭕"; // סימן לדיסק בלתי הפיך
    }

    /**
     * Determines if this disc can be flipped.
     *
     * @return Always false for an UnflippableDisc.
     */
    public boolean canBeFlipped() {
        return false;
    }


}