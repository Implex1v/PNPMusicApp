<script>
    /** @type {import('./$types').Page} */
    export let data;
</script>

<svelte:head>
    <title>All songs</title>
</svelte:head>

<p>A list of all songs available:</p>

<ul>
    {#each data.songs as song}
        <li>
            <a href="/songs/{song.id}">{song.name}</a>
        </li>
    {/each}
</ul>
